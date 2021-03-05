package com.eahm.learn.framework.datasource.network.implementation

import android.util.Log
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.framework.datasource.network.abstraction.UserFirestoreService
import com.eahm.learn.framework.datasource.network.mappers.UserNetworkMapper
import com.eahm.learn.framework.datasource.network.model.UserNetworkEntity
import com.eahm.learn.utils.logD
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserFirestoreServiceImpl
@Inject constructor(
    private val firestore: FirebaseFirestore,
    private val networkMapper: UserNetworkMapper
) : UserFirestoreService {

    private val TAG = "UserFirestoreServiceImpl"

    override suspend fun insertUser(newUser: User): User? {
        logD(TAG, "insert user ${newUser.name_first} ${newUser.last_name_first}")

        val insertion = firestore
                .collection(USERS_COLLECTION)
                .document(newUser.id)
                .set(
                    networkMapper.mapToEntity(newUser)
                )
                .addOnSuccessListener {
                    logD(TAG,"User ${newUser.id} data successfully created!" )
                }
                .addOnFailureListener{ e ->
                    logD(TAG,"Error creating new user ${e.message}" )
                }

        return if (insertion.isSuccessful) newUser else null
    }

    override suspend fun setProviderId(userId : String, providerId: String): Boolean {
        logD(TAG, "set provider $providerId in $userId")
        if(userId.isEmpty() || providerId.isEmpty()) return false

        val update = firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .update(USER_PROVIDER_FIELD, providerId)
                .await()

        //todo improve without 2 network requests
        val checker = firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .await()
                .toObject(UserNetworkEntity::class.java)


        val result =  checker?.let {
            logD(TAG, "our data snap is ${checker.id} - $providerId")
            it.providerId == providerId
        } ?: false

        logD(TAG, "result is $result")
        return result
    }

    override suspend fun getUser(userId: String): User? {
        return firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .get()
            .addOnSuccessListener {
                logD(TAG, "User $userId successfully retrieved! ${it.data}")
            }
            .addOnFailureListener{ e ->
                logD(TAG, "Error getting document ${e.message}")
            }
            .await()
            .toObject(UserNetworkEntity::class.java)?.let {
                networkMapper.mapFromEntity(it)
            }
    }

    companion object{
        const val USERS_COLLECTION = "users"
        const val USER_PROVIDER_FIELD = "providerId"
    }
}