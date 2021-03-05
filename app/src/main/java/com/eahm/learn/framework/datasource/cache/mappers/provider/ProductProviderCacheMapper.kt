package com.eahm.learn.framework.datasource.cache.mappers.provider

import com.eahm.learn.framework.datasource.cache.model.provider.ProductProviderCacheEntity
import java.util.*

class ProductProviderCacheMapper {

    fun mapToEntity(productId: String): ProductProviderCacheEntity {
        return ProductProviderCacheEntity(
            id = UUID.randomUUID().toString(),
            productId = productId
        )
    }

    fun mapFromEntityList(it: List<ProductProviderCacheEntity>) : List<String> {
        return it.map {
            it.productId
        }
    }
}