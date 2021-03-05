package com.eahm.learn.business.data.network.implementation.provider

import com.eahm.learn.business.data.network.abstraction.provider.OrderProviderNetworkDataSource
import com.eahm.learn.business.domain.model.provider.OrderProvider
import com.eahm.learn.framework.datasource.network.abstraction.provider.OrderProviderFirebaseService

class OrderProviderNetworkDataSourceImpl(
    private val orderProviderFirebaseService : OrderProviderFirebaseService
) : OrderProviderNetworkDataSource {

    override suspend fun getOrders(providerId : String): List<OrderProvider> {
        return orderProviderFirebaseService.getOrders(providerId)
    }

    override suspend fun deleteOrder(orderId: String): Int {
        return orderProviderFirebaseService.deleteOrder(orderId)
    }

    override suspend fun setDispatchedOrder(orderId: String): OrderProvider {
        return orderProviderFirebaseService.setDispatchedOrder(orderId)
    }


}