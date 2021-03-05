package com.eahm.learn.framework.presentation.profile.manager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eahm.learn.business.data.cache.abstraction.ShoppingCartCacheDataSource
import com.eahm.learn.business.data.network.abstraction.ShoppingCartNetworkDataSource
import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.utils.logD
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingCartSyncManager
@Inject
constructor(
    private val shoppingCartCacheDataSource: ShoppingCartCacheDataSource,
    private val shoppingCartNetworkDataSource: ShoppingCartNetworkDataSource
) {
    private val TAG = "ShoppingCartSyncManager"

    private val cartSynchronized : MutableLiveData<List<ShoppingCart>?> = MutableLiveData(null)
    private val syncMessages : MutableLiveData<String> = MutableLiveData("")

    val isCartSynchronized : LiveData<List<ShoppingCart>?>
        get() = cartSynchronized

    val messages : LiveData<String>
        get() = syncMessages


    suspend fun syncShoppingCart(userId : String) {
        logD(TAG, "sync shopping cart")

        if(userId.isEmpty()){
            logD(TAG, "user id is empty")
            syncMessages.postValue(SYNC_SHOP_CART_USER_INVALID)
            return
        }

        val shopCartServer = shoppingCartNetworkDataSource.getAllShoppingCart(userId).toMutableList()
        val shopCartLocal = shoppingCartCacheDataSource.getAllShoppingCart(isLocal = true).toMutableList()
        logD(TAG, "local elements: ${shopCartLocal.size} and remote elements: ${shopCartServer.size}")

        //logD(TAG, "clean local shopping cart")
        //shoppingCartCacheDataSource.cleanShoppingCart(isLocal = true)

        for(local in shopCartLocal){
            logD(TAG, "check if the local elements are already saved in the server cart")
            val item = shopCartServer.find {
                it.product.id == local.product.id
            }

            if(item != null){
                logD(TAG, "this element is already in the server")
                // we can update the amount and sum their values
            }
            else {
                logD(TAG, "this element is NOT in the server. Save it.")
                shoppingCartNetworkDataSource.insertInShoppingCart(userId, local)?.let {
                    // Add the new element to our server list
                    shopCartServer.add(it)
                }
            }
        }

        logD(TAG, "insert the new values in cache")
        for(item in shopCartServer){
            shoppingCartCacheDataSource.insertInShoppingCart(item, false)
        }

        logD(TAG, "total in cache and server: ${shopCartServer.size}")
        setSyncCart(shopCartServer)
    }



    private fun setSyncCart(value : List<ShoppingCart>?){
        cartSynchronized.postValue(value)
    }

    companion object{
        const val SYNC_SHOP_CART_NO_SESSION = "there's no valid session available"
        const val SYNC_SHOP_CART_USER_INVALID = "current user is not valid"
    }

}