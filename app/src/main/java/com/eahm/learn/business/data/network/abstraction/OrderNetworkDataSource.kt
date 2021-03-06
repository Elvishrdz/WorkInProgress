package com.eahm.learn.business.data.network.abstraction

import com.eahm.learn.business.domain.model.Delivery
import com.eahm.learn.business.domain.model.Order


interface OrderNetworkDataSource {

    suspend fun placeOrder(newOrder : Order) : String
    suspend fun cancelExistingOrder(orderId : String) : Boolean
    suspend fun updateDeliveryInformation(newDelivery : Delivery) : Boolean
    suspend fun getActiveOrders(userId : String) : List<Order>
    // Not allowed to modify an existing order


}