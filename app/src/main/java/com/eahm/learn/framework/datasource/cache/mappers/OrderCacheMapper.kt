package com.eahm.learn.framework.datasource.cache.mappers

import com.eahm.learn.business.domain.factory.OrderFactory
import com.eahm.learn.business.domain.model.Delivery
import com.eahm.learn.business.domain.model.Order
import com.eahm.learn.business.domain.model.OrderProductList
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.cache.model.OrderCacheEntity
import com.eahm.learn.utils.logD
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type
import javax.inject.Inject

class OrderCacheMapper
@Inject constructor(
    private val orderFactory: OrderFactory,
    private val gson : Gson
) : EntityMapper<OrderCacheEntity, Order> {

    private val TAG = "OrderCacheMapper"

    override fun mapFromEntity(entityModel: OrderCacheEntity): Order {
        val delivery = gson.fromJson(entityModel.delivery, Delivery::class.java)

        val productsType : Type = object : TypeToken<ArrayList<OrderProductList>>(){}.type
        val products : List<OrderProductList> = gson.fromJson(entityModel.products, productsType)

        return orderFactory.createOrder(
            order = entityModel,
            delivery = delivery,
            products = products
        ).also {
            logD(TAG, "mapped to order in domain : $it")
        }
    }

    override fun mapToEntity(domainModel: Order): OrderCacheEntity {
        val delivery = gson.toJson(domainModel.delivery)
        val products = gson.toJson(domainModel.products)

        return OrderCacheEntity(
            id = domainModel.id,
            delivery = delivery,
            order_date = domainModel.order_date,
            products = products,
            clientId = domainModel.clientId,
            orderOrigin = domainModel.orderOrigin,
            status = domainModel.status
        ).also {
            logD(TAG, "mapped to order cache entity: $it")
        }
    }
}