package com.eahm.learn.framework.datasource.cache.dao.provider

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eahm.learn.business.interactors.productlist.InsertNewProduct
import com.eahm.learn.framework.datasource.cache.model.provider.OrderProviderCacheEntity

@Dao
interface OrderProviderDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(newOrder : OrderProviderCacheEntity) : Long

    @Query("UPDATE orders_provider SET address = :newAddress, productId = :newProduct WHERE id = :orderId")
    suspend fun updateOrder(orderId : String, newAddress : String, newProduct: String) : Int

    @Query("DELETE FROM orders_provider WHERE id = :orderId")
    suspend fun removeOrder(orderId : String) : Int

    @Query("DELETE FROM orders_provider")
    suspend fun deleteAllOrders() : Int

    @Query("SELECT * FROM orders_provider WHERE id = :orderId")
    suspend fun getOrder(orderId : String) : OrderProviderCacheEntity?

    @Query("SELECT * FROM orders_provider")
    suspend fun getAllOrders( ) : List<OrderProviderCacheEntity>

}