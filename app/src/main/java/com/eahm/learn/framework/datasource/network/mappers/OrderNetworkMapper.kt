package com.eahm.learn.framework.datasource.network.mappers

import com.eahm.learn.business.domain.model.Delivery
import com.eahm.learn.business.domain.model.Order
import com.eahm.learn.business.domain.model.OrderProductList
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.network.model.DeliveryNetworkEntity
import com.eahm.learn.framework.datasource.network.model.OrderNetworkEntity
import com.eahm.learn.framework.datasource.network.model.OrderProductEntityList

class OrderNetworkMapper : EntityMapper<OrderNetworkEntity, Order> {

    override fun mapFromEntity(entityModel: OrderNetworkEntity): Order {
        return Order(
            id = entityModel.id,
            delivery = Delivery(
                    address = entityModel.delivery.address,
                    city = entityModel.delivery.city,
                    country = entityModel.delivery.country,
                    description = entityModel.delivery.description,
                    postCode = entityModel.delivery.postCode,
                    town = entityModel.delivery.town
            ),
            order_date = entityModel.order_date,
            products = entityModel.products.map {
                    mapProductsFromEntity(it)
            },
            clientId = entityModel.clientId,
            orderOrigin = entityModel.orderOrigin,
            status = entityModel.status,
        )
    }

    override fun mapToEntity(domainModel: Order): OrderNetworkEntity {
        return OrderNetworkEntity(
                id = domainModel.id,
                delivery = DeliveryNetworkEntity(
                        address = domainModel.delivery.address,
                        city = domainModel.delivery.city,
                        country = domainModel.delivery.country,
                        description = domainModel.delivery.description,
                        postCode = domainModel.delivery.postCode,
                        town = domainModel.delivery.town
                ),
                order_date = domainModel.order_date,
                products = domainModel.products.map {
                    mapProductsToEntity(it)
                },
                clientId = domainModel.clientId,
                orderOrigin = domainModel.orderOrigin,
                status = domainModel.status,
        )
    }

    fun mapProductsToEntity(product : OrderProductList) : OrderProductEntityList  = OrderProductEntityList(
        amount = product.amount,
        productId = product.productId,
        orderId = product.orderId,
    )

    fun mapProductsFromEntity(product : OrderProductEntityList) : OrderProductList = OrderProductList(
        amount = product.amount,
        productId = product.productId,
        orderId = product.orderId,
    )


}