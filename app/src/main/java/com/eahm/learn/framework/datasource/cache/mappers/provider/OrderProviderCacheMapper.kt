package com.eahm.learn.framework.datasource.cache.mappers.provider

import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.factory.provider.OrderProviderFactory
import com.eahm.learn.business.domain.model.Address
import com.eahm.learn.business.domain.model.Location
import com.eahm.learn.business.domain.model.provider.OrderProvider
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.cache.model.provider.OrderProviderCacheEntity
import com.google.gson.Gson
import javax.inject.Inject

class OrderProviderCacheMapper
@Inject constructor(
    private val productFactory: ProductFactory,
    private val orderProviderFactory: OrderProviderFactory,
    private val gson: Gson
) : EntityMapper<OrderProviderCacheEntity, OrderProvider> {

    override fun mapFromEntity(entityModel: OrderProviderCacheEntity): OrderProvider {
        val address : Address = gson.fromJson(entityModel.address, Address::class.java) ?: Address("","","","","", Location(0.0,0.0))

        val newOrderProvider = orderProviderFactory.createOrderProvider(
                id = entityModel.id,
                address = address,
                productId = entityModel.id
        )

        return newOrderProvider
    }

    override fun mapToEntity(domainModel: OrderProvider): OrderProviderCacheEntity {
        val address = gson.toJson(domainModel.address)

        return OrderProviderCacheEntity(
                id = domainModel.id,
                address = address,
                productId = domainModel.productId.id
        )
    }



}