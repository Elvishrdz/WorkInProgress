package com.eahm.learn.business.data.cache.implementation

import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.business.data.cache.abstraction.ShoppingCartCacheDataSource
import com.eahm.learn.framework.datasource.cache.abstraction.ShoppingCartDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingCartCacheDataSourceImpl
@Inject constructor(
    private val shoppingCartDaoService : ShoppingCartDaoService
) : ShoppingCartCacheDataSource{

    override suspend fun getAllShoppingCart(isLocal : Boolean): List<ShoppingCart>
    = shoppingCartDaoService.getAllShoppingCart(isLocal)

    override suspend fun insertInShoppingCart(newProduct: ShoppingCart, isLocal : Boolean) : Long
    = shoppingCartDaoService.insertInShoppingCart(newProduct, isLocal)

    override suspend fun removeFromShoppingCart(shoppingCartId: String): Int
    = shoppingCartDaoService.removeFromShoppingCart(shoppingCartId)

    override suspend fun cleanShoppingCart(isLocal : Boolean): Int =
        shoppingCartDaoService.cleanShoppingCart(isLocal)

    override suspend fun updateAmount(shoppingCartId: String, newAmount: Int): Int
    = shoppingCartDaoService.updateAmount(shoppingCartId, newAmount)

    override suspend fun searchItem(shoppingCartId: String): ShoppingCart?
    = shoppingCartDaoService.searchItem(shoppingCartId)

    override suspend fun getItemsAmount(isLocal : Boolean): Int
    = shoppingCartDaoService.getItemsAmount(isLocal)

}

