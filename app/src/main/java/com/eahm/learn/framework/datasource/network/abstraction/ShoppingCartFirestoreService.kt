package com.eahm.learn.framework.datasource.network.abstraction

import com.eahm.learn.business.domain.model.ShoppingCart

interface ShoppingCartFirestoreService {
    suspend fun getAllShoppingCart(userId : String) : List<ShoppingCart>
    suspend fun insertInShoppingCart(userId : String, newProduct : ShoppingCart) : ShoppingCart?
    suspend fun removeFromShoppingCart(userId :String, shoppingCartId : String) : Int
    suspend fun cleanShoppingCart(userId: String): Int
    suspend fun updateAmount(userId : String, shoppingCartId: String, newAmount : Int) : Int
    suspend fun searchItem(shoppingCartId: String) : ShoppingCart?
    suspend fun getItemsAmount() : Int? // Total Number of Products in the Shopping Cart
}