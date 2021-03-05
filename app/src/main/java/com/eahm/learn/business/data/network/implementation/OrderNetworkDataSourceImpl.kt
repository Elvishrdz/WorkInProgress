package com.eahm.learn.business.data.network.implementation

import com.eahm.learn.business.data.network.abstraction.OrderNetworkDataSource
import com.eahm.learn.business.domain.model.Delivery
import com.eahm.learn.business.domain.model.Order
import com.eahm.learn.framework.datasource.network.abstraction.OrderFirestoreService

class OrderNetworkDataSourceImpl(
    private val orderFirestoreService : OrderFirestoreService
) : OrderNetworkDataSource{

    override suspend fun placeOrder(newOrder: Order): String {
        return orderFirestoreService.placeOrder(newOrder)
    }

    override suspend fun cancelExistingOrder(orderId: String): Boolean {
        return orderFirestoreService.cancelExistingOrder(orderId)
    }

    override suspend fun updateDeliveryInformation(newDelivery: Delivery): Boolean {
        return orderFirestoreService.updateDeliveryInformation(newDelivery)
    }

    override suspend fun getActiveOrders(userId : String): List<Order> {
        return orderFirestoreService.getActiveOrders(userId)
    }
}