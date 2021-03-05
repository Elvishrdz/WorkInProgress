package com.eahm.learn.business.data.cache.abstraction.provider

import com.eahm.learn.business.domain.model.provider.OrderProvider

interface OrderProviderCacheDataSource {

    suspend fun insertOrder(newOrder : OrderProvider) : Long
    suspend fun updateOrder(newOrderValues : OrderProvider) : Int
    suspend fun deleteOrder(orderId : String) : Int
    suspend fun cleanOrders() : Int
    suspend fun getOrder(orderId : String) : OrderProvider?
    suspend fun getOrders() : List<OrderProvider>
}