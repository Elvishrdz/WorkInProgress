package com.eahm.learn.framework.datasource.cache.mappers

import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.cache.model.ShoppingCartCacheEntity
import javax.inject.Singleton

@Singleton
class ShoppingCartCacheMapper(
    private val productFactory : ProductFactory
) : EntityMapper<ShoppingCartCacheEntity, ShoppingCart>{

    override fun mapFromEntity(entityModel: ShoppingCartCacheEntity): ShoppingCart =
        ShoppingCart(
            id = entityModel.id,
            product = productFactory.createEmptyProduct(entityModel.id),
            amount = entityModel.amount
        )

    override fun mapToEntity(domainModel: ShoppingCart): ShoppingCartCacheEntity =
        ShoppingCartCacheEntity(
            id = domainModel.id,
            productID = domainModel.product.id,
            amount = domainModel.amount,
            isLocal = false
        )

    fun mapToEntity(domainModel: ShoppingCart, isLocal :Boolean): ShoppingCartCacheEntity =
        ShoppingCartCacheEntity(
            id = domainModel.id,
            productID = domainModel.product.id,
            amount = domainModel.amount,
            isLocal = isLocal
        )


}