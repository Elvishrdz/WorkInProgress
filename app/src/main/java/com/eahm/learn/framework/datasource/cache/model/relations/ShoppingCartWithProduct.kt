package com.eahm.learn.framework.datasource.cache.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.eahm.learn.framework.datasource.cache.model.ProductCacheEntity
import com.eahm.learn.framework.datasource.cache.model.ShoppingCartCacheEntity

/**
 * A one to one relationship
 */
data class ShoppingCartWithProduct(
    @Embedded
    val shoppingCart : ShoppingCartCacheEntity,

    @Relation(
        parentColumn = "productID",
        entityColumn = "id"
    )
    val product : ProductCacheEntity
)