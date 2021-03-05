package com.eahm.learn.framework.datasource.cache.abstraction

import com.eahm.learn.business.domain.model.ShoppingCart

interface ShoppingCartDaoService {
    suspend fun getAllShoppingCart(isLocal : Boolean) : List<ShoppingCart>
    suspend fun insertInShoppingCart(newProduct : ShoppingCart, isLocal : Boolean) : Long
    suspend fun removeFromShoppingCart(shoppingCartId : String) : Int
    suspend fun cleanShoppingCart(isLocal : Boolean) : Int
    suspend fun updateAmount(shoppingCartId: String, newAmount : Int) : Int
    suspend fun searchItem(productId: String) : ShoppingCart?
    suspend fun getItemsAmount(isLocal : Boolean) : Int // Total Number of Products in the Shopping Cart

}