package com.eahm.learn.framework.datasource.cache.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.eahm.learn.framework.datasource.cache.model.ProductCacheEntity
import com.eahm.learn.framework.datasource.cache.model.provider.OrderProviderCacheEntity


data class OrderWithProduct (

    @Embedded
    val order : OrderProviderCacheEntity,

        @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product : ProductCacheEntity
)