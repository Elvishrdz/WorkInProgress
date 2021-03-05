package com.eahm.learn.business.domain.factory.provider

import com.eahm.learn.business.domain.factory.AddressFactory
import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.model.Address
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.model.provider.OrderProvider
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderProviderFactory
@Inject
constructor(
    private val addressFactory : AddressFactory,
    private val productFactory: ProductFactory
){

    fun createOrderProvider(
            id: String,
            address : Address? = null,
            productId : String = "",
            productOrdered : Product = productFactory.createEmptyProduct()
    ): OrderProvider {
        return OrderProvider(
                id = if(id.isNotEmpty()) id else UUID.randomUUID().toString(),
                address = address ?: addressFactory.createAddress(),
                productId = productFactory.createEmptyProduct(productId),
                productOrdered = productOrdered
        )
    }

}