package com.eahm.learn.business.domain.factory

import com.eahm.learn.business.domain.model.Delivery
import com.eahm.learn.business.domain.model.Order
import com.eahm.learn.business.domain.model.OrderProductList
import com.eahm.learn.framework.datasource.cache.model.OrderCacheEntity
import javax.inject.Singleton

@Singleton
class OrderFactory {

    fun createOrder(
            products : List<OrderProductList>
    ) : Order {
        return Order(
                id = "1",
                delivery = Delivery(
                        address = "2",
                        city = "3",
                        country = "4",
                        description = "5",
                        postCode = "6",
                        town = "7",
                ),
                order_date = "8",
                products = products.toList(),
                clientId = "10",
                orderOrigin = "11",
                status = "12",
        )
    }

    fun createOrder(
            id : String,
            delivery : Delivery,
            products: List<OrderProductList>
    ) : Order {
        return Order(
                id = id,
                delivery = delivery,
                order_date = "8",
                products = products,
                clientId = "10",
                orderOrigin = "11",
                status = "12",
        )
    }

    fun createOrder(
            order : OrderCacheEntity,
            delivery : Delivery,
            products: List<OrderProductList>
    ) : Order {
        return Order(
            id = order.id,
            delivery = delivery,
            order_date = order.order_date,
            products = products,
            clientId = order.clientId,
            orderOrigin = order.orderOrigin,
            status = order.status,
        )
    }




}