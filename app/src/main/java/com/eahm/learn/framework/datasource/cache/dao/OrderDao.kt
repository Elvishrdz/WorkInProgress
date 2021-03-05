package com.eahm.learn.framework.datasource.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eahm.learn.framework.datasource.cache.model.OrderCacheEntity

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(newOrder : OrderCacheEntity) : Long

    @Query("DELETE FROM orders WHERE id = :orderId")
    suspend fun deleteOrder(orderId : String): Int

    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders(): Int

    @Query("SELECT * FROM orders WHERE id =:orderId")
    suspend fun getOrder(orderId : String): OrderCacheEntity?

    @Query("SELECT * FROM orders")
    suspend fun getOrders(): List<OrderCacheEntity>

    @Query("""
        UPDATE orders 
        SET 
        delivery = :delivery,  
        products = :products,
        status = :status
        WHERE id = :orderId
        """
    )
    suspend fun updateOrder(
        orderId: String,
        delivery: String,
        products: String,
        status: String
    ): Int
}