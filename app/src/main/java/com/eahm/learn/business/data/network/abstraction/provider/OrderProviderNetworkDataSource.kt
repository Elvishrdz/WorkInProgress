package com.eahm.learn.business.data.network.abstraction.provider

import com.eahm.learn.business.domain.model.provider.OrderProvider

interface OrderProviderNetworkDataSource {
    suspend fun getOrders(providerId : String) : List<OrderProvider>
    suspend fun deleteOrder(orderId : String) : Int
    suspend fun setDispatchedOrder(orderId: String) : OrderProvider
}