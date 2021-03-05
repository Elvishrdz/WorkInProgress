package com.eahm.learn.framework.datasource.network.mappers

import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.network.model.ShoppingCartNetworkEntity
import javax.inject.Inject

class ShoppingCartNetworkMapper
@Inject
constructor(
      private val  productFactory: ProductFactory
) : EntityMapper<ShoppingCartNetworkEntity, ShoppingCart> {

    override fun mapFromEntity(entityModel: ShoppingCartNetworkEntity): ShoppingCart {
        return ShoppingCart(
            id = entityModel.id,
            product = productFactory.createEmptyProduct(entityModel.productId),
            amount = entityModel.amount,
        )
    }

    override fun mapToEntity(domainModel: ShoppingCart): ShoppingCartNetworkEntity {
        return ShoppingCartNetworkEntity(
                id = domainModel.id,
                productId = domainModel.product.id,
                amount = domainModel.amount,
        )
    }
}