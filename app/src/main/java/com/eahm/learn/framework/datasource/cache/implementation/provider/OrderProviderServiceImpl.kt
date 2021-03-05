package com.eahm.learn.framework.datasource.cache.implementation.provider

import com.eahm.learn.business.domain.model.provider.OrderProvider
import com.eahm.learn.framework.datasource.cache.abstraction.provider.OrderProviderDaoService
import com.eahm.learn.framework.datasource.cache.dao.provider.OrderProviderDao
import com.eahm.learn.framework.datasource.cache.mappers.provider.OrderProviderCacheMapper
import com.eahm.learn.utils.logD

class OrderProviderServiceImpl(
    private val orderProviderDao : OrderProviderDao,
    private val orderProviderCacheMapper : OrderProviderCacheMapper
): OrderProviderDaoService {

    private val TAG = "OrderProviderServiceImpl"

    override suspend fun insertOrder(newOrder: OrderProvider): Long {
        return orderProviderDao.insertOrder(orderProviderCacheMapper.mapToEntity(newOrder))
    }

    override suspend fun updateOrder(newOrderValues: OrderProvider): Int {
        return orderProviderCacheMapper.mapToEntity(newOrderValues).let {
            orderProviderDao.updateOrder(
                    it.id,
                    it.address,
                    it.productId
            )
        }
    }

    override suspend fun deleteOrder(orderId: String): Int {
        return orderProviderDao.removeOrder(orderId)
    }

    override suspend fun cleanOrders(): Int {
        return orderProviderDao.deleteAllOrders()
    }

    override suspend fun getOrder(orderId: String): OrderProvider? {
        return orderProviderDao.getOrder(orderId)?.let {
            orderProviderCacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getOrders(): List<OrderProvider> {
        logD(TAG, "get orders")
        return orderProviderDao.getAllOrders().map {
            logD(TAG, "orders from cache: ${it.id}")
            orderProviderCacheMapper.mapFromEntity(it)
        }
    }
}