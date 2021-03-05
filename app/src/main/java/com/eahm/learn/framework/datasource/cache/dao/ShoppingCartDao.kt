package com.eahm.learn.framework.datasource.cache.dao

import androidx.room.*
import com.eahm.learn.framework.datasource.cache.model.ShoppingCartCacheEntity
import com.eahm.learn.framework.datasource.cache.model.relations.ShoppingCartWithProduct

@Dao
interface ShoppingCartDao {

    /*@Transaction
    @Query("SELECT * FROM shopping_cart")
    suspend fun getAllShoppingCartWithProduct() : List<ShoppingCartWithProduct>*/

    @Transaction
    @Query("SELECT * FROM shopping_cart WHERE isLocal = 1")
    suspend fun getAllLocalShoppingCart() : List<ShoppingCartWithProduct>

    @Transaction
    @Query("SELECT * FROM shopping_cart WHERE isLocal = 0")
    suspend fun getAllNonLocalShoppingCart() : List<ShoppingCartWithProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInShoppingCart(newProduct : ShoppingCartCacheEntity) : Long

    @Query("DELETE FROM shopping_cart WHERE id = :shoppingCartId ")
    suspend fun removeFromShoppingCart(shoppingCartId : String) : Int

    @Query("DELETE FROM shopping_cart WHERE isLocal == 1")
    suspend fun deleteLocals() : Int

    @Query("DELETE FROM shopping_cart WHERE isLocal == 0")
    suspend fun deleteNonLocals() : Int

    @Query("UPDATE shopping_cart SET amount = :newAmount WHERE id = :shoppingCartId")
    suspend fun updateAmount(shoppingCartId: String, newAmount : Int) : Int

    @Query("SELECT * FROM shopping_cart WHERE productID = :productId")
    suspend fun searchItem(productId: String) : ShoppingCartCacheEntity?

    @Query("SELECT COUNT(*) FROM shopping_cart WHERE isLocal = 1")
    suspend fun getLocalItemsAmount() : Int // Total Number of local Products in the Shopping Cart

    @Query("SELECT COUNT(*) FROM shopping_cart WHERE isLocal = 0")
    suspend fun getNonLocalItemsAmount() : Int // Total Number of NON local Products in the Shopping Cart

}