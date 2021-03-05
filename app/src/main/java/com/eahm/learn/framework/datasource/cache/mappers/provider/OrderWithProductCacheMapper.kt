package com.eahm.learn.framework.datasource.cache.mappers.provider

import com.eahm.learn.business.domain.factory.provider.OrderProviderFactory
import com.eahm.learn.business.domain.model.provider.OrderProvider
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.cache.mappers.ProductCacheMapper
import com.eahm.learn.framework.datasource.cache.model.relations.OrderWithProduct
import com.google.gson.Gson
import javax.inject.Inject

class OrderWithProductCacheMapper
@Inject
constructor(
        private val orderProviderFactory : OrderProviderFactory,
        private val orderProviderCacheMapper: OrderProviderCacheMapper,
        private val productCacheMapper: ProductCacheMapper
) : EntityMapper<OrderWithProduct, OrderProvider> {

    override fun mapFromEntity(entityModel: OrderWithProduct): OrderProvider {
        val orderCache = orderProviderCacheMapper.mapFromEntity(entityModel.order)


        return OrderProvider(
                id = orderCache.id,
                address = orderCache.address,
                productId = productCacheMapper.mapFromEntity(entityModel.product),
                productOrdered = orderCache.productOrdered
        )
    }

    override fun mapToEntity(domainModel: OrderProvider): OrderWithProduct {
        return OrderWithProduct(
                order = orderProviderCacheMapper.mapToEntity(
                            orderProviderFactory.createOrderProvider(
                                    domainModel.id,
                                    domainModel.address,
                                    domainModel.productId.id,
                                    domainModel.productOrdered
                            )
                        ),
                product = productCacheMapper.mapToEntity(domainModel.productId)
        )
    }

}