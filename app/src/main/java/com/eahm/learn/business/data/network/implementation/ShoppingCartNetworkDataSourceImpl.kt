package com.eahm.learn.business.data.network.implementation

import  com.eahm.learn.business.data.network.abstraction.ShoppingCartNetworkDataSource
import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.framework.datasource.network.abstraction.ShoppingCartFirestoreService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingCartNetworkDataSourceImpl
@Inject
constructor(
    private val shoppingCartFirestoreService : ShoppingCartFirestoreService
) : ShoppingCartNetworkDataSource {

    override suspend fun getAllShoppingCart(userId : String): List<ShoppingCart> {
        return shoppingCartFirestoreService.getAllShoppingCart(userId)
    }

    override suspend fun insertInShoppingCart(userId : String, newProduct: ShoppingCart): ShoppingCart? {
        return shoppingCartFirestoreService.insertInShoppingCart(userId, newProduct)
    }

    override suspend fun removeFromShoppingCart(userId :String, shoppingCartId: String): Int {
        return shoppingCartFirestoreService.removeFromShoppingCart(userId, shoppingCartId)
    }

    override suspend fun cleanShoppingCart(userId: String): Int {
        return shoppingCartFirestoreService.cleanShoppingCart(userId)
    }

    override suspend fun updateAmount(userId : String, shoppingCartId: String, newAmount: Int): Int {
        return shoppingCartFirestoreService. updateAmount(userId, shoppingCartId, newAmount)
    }

    override suspend fun searchItem(shoppingCartId: String): ShoppingCart? {
        return shoppingCartFirestoreService.searchItem(shoppingCartId)
    }

    override suspend fun getItemsAmount(): Int? {
        return shoppingCartFirestoreService.getItemsAmount()
    }
}