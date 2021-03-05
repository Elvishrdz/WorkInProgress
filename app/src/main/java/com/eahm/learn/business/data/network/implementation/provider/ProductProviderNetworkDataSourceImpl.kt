package com.eahm.learn.business.data.network.implementation.provider

import com.eahm.learn.business.data.network.abstraction.provider.ProductProviderNetworkDataSource
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.framework.datasource.network.abstraction.provider.ProductProviderFirestoreService

class ProductProviderNetworkDataSourceImpl(
    private val productProviderFirestoreService : ProductProviderFirestoreService
) : ProductProviderNetworkDataSource {

    override suspend fun insertProductProvider(providerId: String, productId: String): Boolean {
        return productProviderFirestoreService.insertProductProvider(providerId, productId)
    }

    override suspend fun getAllProductsProviderIds(providerId: String): List<Product> {
        return productProviderFirestoreService.getAllProductsProviderIds(providerId)
    }

    override suspend fun deleteProductsProvider(providerId: String, productId : String): Int {
        return productProviderFirestoreService.deleteProductProvider(providerId, productId)
    }
}