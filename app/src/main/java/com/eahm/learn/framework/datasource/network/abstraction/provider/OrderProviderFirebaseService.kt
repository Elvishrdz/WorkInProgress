package com.eahm.learn.framework.datasource.network.abstraction.provider

import com.eahm.learn.business.domain.model.provider.OrderProvider

interface OrderProviderFirebaseService {
    suspend fun getOrders(providerId : String) : List<OrderProvider>
    suspend fun deleteOrder(orderId : String) : Int
    suspend fun setDispatchedOrder(orderId: String) : OrderProvider
}