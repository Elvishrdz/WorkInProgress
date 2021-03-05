package com.eahm.learn.framework.datasource.network.mappers.provider

import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.model.provider.OrderProvider
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.network.model.provider.OrderProviderNetworkEntity
import com.google.gson.Gson

class OrderProviderNetworkMapper(
    private val addressNetworkMapper : AddressNetworkMapper,
    private val productFactory: ProductFactory,
    private val gson : Gson
) : EntityMapper<OrderProviderNetworkEntity, OrderProvider> {

    override fun mapFromEntity(entityModel: OrderProviderNetworkEntity): OrderProvider {
        val productOrdered = gson.fromJson(entityModel.productOrdered, Product::class.java)

        return OrderProvider(
            id = entityModel.id,
            address = addressNetworkMapper.mapFromEntity(entityModel.address),
            productId = productFactory.createEmptyProduct(entityModel.productId),
            productOrdered = productOrdered
        )
    }

    override fun mapToEntity(domainModel: OrderProvider): OrderProviderNetworkEntity {
        val productOrdered = gson.toJson(domainModel.productOrdered)

        return OrderProviderNetworkEntity(
            id = domainModel.id,
            productId = domainModel.productId.id,
            address = addressNetworkMapper.mapToEntity(domainModel.address),
            productOrdered = productOrdered
        )
    }
}