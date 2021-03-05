package com.eahm.learn.business.data.cache.implementation.provider

import com.eahm.learn.business.data.cache.abstraction.provider.OrderProviderCacheDataSource
import com.eahm.learn.business.domain.model.provider.OrderProvider
import com.eahm.learn.framework.datasource.cache.abstraction.provider.OrderProviderDaoService

class OrderProviderCacheDataSourceImpl(
    private val orderProviderDaoService : OrderProviderDaoService
): OrderProviderCacheDataSource {

    override suspend fun insertOrder(newOrder: OrderProvider): Long {
        return orderProviderDaoService.insertOrder(newOrder)
    }

    override suspend fun updateOrder(newOrderValues: OrderProvider): Int {
        return orderProviderDaoService.updateOrder(newOrderValues)
    }

    override suspend fun deleteOrder(orderId: String): Int {
        return orderProviderDaoService.deleteOrder(orderId)
    }

    override suspend fun cleanOrders(): Int {
        return orderProviderDaoService.cleanOrders()
    }

    override suspend fun getOrder(orderId: String): OrderProvider? {
        return orderProviderDaoService.getOrder(orderId)
    }

    override suspend fun getOrders(): List<OrderProvider> {
        return orderProviderDaoService.getOrders()
    }
}