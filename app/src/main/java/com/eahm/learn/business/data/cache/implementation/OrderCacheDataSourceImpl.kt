package com.eahm.learn.business.data.cache.implementation

import com.eahm.learn.business.data.cache.abstraction.OrderCacheDataSource
import com.eahm.learn.business.domain.model.Order
import com.eahm.learn.framework.datasource.cache.abstraction.OrderDaoService
import javax.inject.Inject

class OrderCacheDataSourceImpl
@Inject constructor(
    private val orderDaoService : OrderDaoService
) : OrderCacheDataSource {

    override suspend  fun insertOrder(newOrder: Order): Long {
        return orderDaoService.insertOrder(newOrder)
    }

    override suspend  fun deleteOrder(orderId: String): Int {
        return orderDaoService.deleteOrder(orderId)
    }

    override suspend fun cleanOrders(): Int {
        return orderDaoService.cleanOrders()
    }

    override suspend  fun getOrder(orderId: String): Order? {
        return orderDaoService.getOrder(orderId)
    }

    override suspend  fun updateOrder(order: Order): Int {
        return orderDaoService.updateOrder(order)
    }

    override suspend  fun getOrders(): List<Order> {
        return orderDaoService.getOrders()
    }
}