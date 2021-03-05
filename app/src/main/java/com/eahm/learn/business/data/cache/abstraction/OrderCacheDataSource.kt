package com.eahm.learn.business.data.cache.abstraction

import com.eahm.learn.business.domain.model.Order

interface OrderCacheDataSource {
    suspend fun insertOrder(newOrder : Order) : Long
    suspend fun deleteOrder(orderId : String) : Int
    suspend fun cleanOrders() : Int
    suspend fun getOrder(orderId : String) : Order?
    suspend fun updateOrder(order : Order) : Int
    suspend fun getOrders() : List<Order>
}