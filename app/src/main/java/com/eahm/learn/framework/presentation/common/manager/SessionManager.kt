package com.eahm.learn.framework.presentation.common.manager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eahm.learn.business.data.cache.abstraction.OrderCacheDataSource
import com.eahm.learn.business.data.cache.abstraction.ProviderCacheDataSource
import com.eahm.learn.business.data.cache.abstraction.ShoppingCartCacheDataSource
import com.eahm.learn.business.data.cache.abstraction.UserCacheDataSource
import com.eahm.learn.business.data.cache.abstraction.provider.OrderProviderCacheDataSource
import com.eahm.learn.business.data.network.abstraction.UserNetworkDataSource
import com.eahm.learn.business.domain.factory.UserFactory
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.framework.presentation.profile.manager.ShoppingCartSyncManager
import com.eahm.learn.utils.logD
import com.google.firebase.auth.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userCacheDataSource: UserCacheDataSource,
    private val userNetworkDataSource: UserNetworkDataSource,
    private val userFactory: UserFactory,
    private val shoppingCartSyncManager : ShoppingCartSyncManager,

    private val shoppingCartCacheDataSource: ShoppingCartCacheDataSource,
    private val orderProviderCacheDataSource: OrderProviderCacheDataSource,
    private val orderCacheDataSource: OrderCacheDataSource
) : FirebaseAuth.IdTokenListener,
    FirebaseAuth.AuthStateListener,
    ISessionManager {

    private val TAG = "SessionManager"

    private val _cachedUser = MutableLiveData<User?>(userFactory.createEmptyUser())
    private val _authMessage = MutableLiveData<String?>()

    val cachedUser : LiveData<User?>
        get() = _cachedUser

    val authMessage : LiveData<String?>
        get() = _authMessage

    val isActiveSession: Boolean
        get() = firebaseAuth.currentUser != null

    init {
        firebaseAuth.addAuthStateListener(this)
        firebaseAuth.addIdTokenListener(this)

        initUser(firebaseAuth.currentUser?.uid ?: "")
    }

    private fun initUser(userId: String) {
        CoroutineScope(IO).launch {
            initSession(userId)
        }
    }

    fun onClearedSessionManager(){
        // call it on your on destroy views
        cleanListeners()
    }

    private fun cleanListeners(){
        firebaseAuth.removeAuthStateListener(this)
        firebaseAuth.removeIdTokenListener(this)
    }

    override fun authenticate(email :String, password : String){
        if(!isActiveSession){
            logD(TAG, "authenticate $email $password")
            CoroutineScope(IO).launch {
                authFlow(email, password)
                    .collect { user ->
                        shoppingCartSyncManager.syncShoppingCart(userId = user?.id ?: "")
                        setCacheUserPostValue(user)
                    }
            }
        }
    }

    override fun logout(){
        logD(TAG, "log out user")
        CoroutineScope(IO).launch {
            logoutFlow()
                .collect { status ->
                    logD(TAG, "sign out: $status")
                    if(status){
                        firebaseAuth.signOut()
                    }
                }
        }

    }

    override fun refreshSession() {
        _cachedUser.value?.let {
            CoroutineScope(IO).launch {
                refreshSession(it)
            }
        }
    }

    private suspend fun refreshSession(user : User){
        logD(TAG, "refresh the session from cache ${user.id}")
        val cachedUser = userCacheDataSource.getClient(user.id)
        if(cachedUser != null){
            setCacheUserValue(cachedUser)
        }
        else {
            // todo obtain from server. Should we do this everytime? set a limit
        }
    }

    private suspend fun initSession(userId : String){
        logD(TAG, "initialize session from cache $userId")

        val cachedUser = if(userId.isNotEmpty()) userCacheDataSource.getClient(userId) else null

        if(cachedUser != null){
            logD(TAG, "session successfully retrieved $cachedUser")
            _cachedUser.postValue(cachedUser)
        }
        else {
            // todo delete session
            logD(TAG, "close and log out session")

            logout()
        }
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        // used when user log in
        logD(TAG, "onAuthStateChanged called $isActiveSession ${auth.currentUser != null}")
        authenticateResult(auth.currentUser)
    }

    override fun onIdTokenChanged(auth: FirebaseAuth) {
        // used when user logout
        logD(TAG, "onIdTokenChanged called $isActiveSession ${auth.currentUser != null}")
        authenticateResult(auth.currentUser)
    }

    private fun authenticateResult(auth :FirebaseUser?){
        if(isActiveSession && auth != null){
            setCacheUserValue(
                userFactory.createUser(
                    id = auth.uid,
                    name = auth.displayName ?: "",
                    email = auth.email ?: ""
                )
            )
        }
        else {
            setCacheUserValue(null)
        }
    }

    private fun setCacheUserValue(user : User?){
        logD(TAG, "cache user value setted ${user?.id}")
        if(cachedUser.value != user) _cachedUser.value = user
    }

    private fun setCacheUserPostValue(user : User?){
        logD(TAG, "cache user post value setted ${user?.id}")
        if(cachedUser.value != user) _cachedUser.postValue(user)
    }

    private fun setAuthMessage(message : String?){
        message?.let {
            logD(TAG, "setAuthMessage called = $message")
            _authMessage.value = message
        }
    }

    fun createAccount(newUser: User, password : String) {
        logD(TAG, "createAccount ${newUser.name_first} ${newUser.last_name_first}")

        CoroutineScope(IO).launch {
            newAccountFlow(newUser, password)
                .collect { user ->
                    _cachedUser.postValue(user)
                }
        }
    }

    private fun logoutFlow() : Flow<Boolean> = flow {
        //val currentClient = cachedUser.value?.id ?: ""
        userCacheDataSource.deleteClient()

        //TODO DELETE HERE ALL USER INFORMATION. SHOPPING CART, PROVIDERS, ORDERS, ETC. EXCEPT PRODUCTS.

        val shop = shoppingCartCacheDataSource.cleanShoppingCart(false)
        val orderP = orderProviderCacheDataSource.cleanOrders()
        val order = orderCacheDataSource.cleanOrders()
        logD(TAG, "logout flow: delete cache values: $shop - $orderP -$order")

        emit(true)
    }

    private fun newAccountFlow(newUser : User, password : String) : Flow<User?> = flow {
        var emitResult : User? = null
        val createAccountResult = firebaseAuth.createUserWithEmailAndPassword(newUser.email, password).await()

        createAccountResult?.user?.let { networkUser ->
            logD(TAG, "Create User Data")

            val userWithID = userFactory.createUser(
                    id = networkUser.uid,
                    name = newUser.name_first,
                    secondName = newUser.name_second,
                    firstLastName = newUser.last_name_first,
                    secondLastName = newUser.last_name_second,
                    email = networkUser.email ?: newUser.email
            )

            val networkUserData = userNetworkDataSource.insertUser(userWithID)

            if(networkUserData != null){
                logD(TAG, "remove any previous profile client from cache")
                userCacheDataSource.deleteClient()

                logD(TAG, "save in cache")
                userCacheDataSource.insertClient(networkUserData)
                emitResult = networkUserData
            }
            else {
                emitResult = userFactory.createUser(
                        id = networkUser.uid,
                        name = networkUser.displayName ?: "",
                        email = networkUser.email ?: "",
                )
            }
        }


        emit(emitResult)
    }

    private fun authFlow(email :String, password : String) : Flow<User?> = flow{
        var emitResult : User? = null

        val result : AuthResult? = firebaseAuth.signInWithEmailAndPassword(email,password).await()

        result?.user?.let { networkUser ->
            logD(TAG, "authenticated! get user data of ${networkUser.uid} from server")
            val networkUserData = userNetworkDataSource.getUser(networkUser.uid)

            if(networkUserData != null){
                logD(TAG, "cache user data locally")
                val currentUser = userFactory.createUser(
                        user = networkUserData,
                        email = email
                )

                currentUser?.let {
                    logD(TAG, "remove any previous profile client from cache")
                    userCacheDataSource.deleteClient()

                    logD(TAG, "save in user cache $currentUser")
                    userCacheDataSource.insertClient(currentUser)

                    emitResult = currentUser
                }
            }
            else{
                logD(TAG, "this user hasn't saved personal data yet")
                emitResult = userFactory.createUser(
                        id = networkUser.uid,
                        name = networkUser.displayName ?: "",
                        email = networkUser.email ?: "",
                )
            }
        }

        logD(TAG, "emit result ${emitResult?.id}")
        emit(emitResult)
    }

    private fun updateUser(){

    }

    private fun restoreClient() {
        // obtain from cache the user when the app start.
    }



}


interface ISessionManager{
    fun authenticate(email :String, password : String)
    fun logout()
    fun refreshSession()
}