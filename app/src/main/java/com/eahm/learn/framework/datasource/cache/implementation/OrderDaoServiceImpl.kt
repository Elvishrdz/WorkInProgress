package com.eahm.learn.framework.datasource.cache.implementation

import com.eahm.learn.business.domain.model.Order
import com.eahm.learn.framework.datasource.cache.abstraction.OrderDaoService
import com.eahm.learn.framework.datasource.cache.dao.OrderDao
import com.eahm.learn.framework.datasource.cache.mappers.OrderCacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderDaoServiceImpl
@Inject constructor(
    private val orderDao : OrderDao,
    private val orderCacheMapper: OrderCacheMapper
) : OrderDaoService {

    override suspend fun insertOrder(newOrder: Order): Long {
        return orderDao.insertOrder(
            orderCacheMapper.mapToEntity(newOrder)
        )
    }

    override suspend fun deleteOrder(orderId: String): Int {
        return orderDao.deleteOrder(orderId)
    }

    override suspend fun cleanOrders(): Int {
        return orderDao.deleteAllOrders()
    }

    override suspend fun getOrder(orderId: String): Order? {
        return orderDao.getOrder(orderId)?.let {
            orderCacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun updateOrder(order: Order): Int {
        val orderCache = orderCacheMapper.mapToEntity(order)
        return orderDao.updateOrder(
            orderId =  orderCache.id,
            delivery = orderCache.delivery,
            products = orderCache.products,
            status = orderCache.status
        )
    }

    override suspend fun getOrders(): List<Order> {
        return orderDao.getOrders().map {
            orderCacheMapper.mapFromEntity(it)
        }
    }
}