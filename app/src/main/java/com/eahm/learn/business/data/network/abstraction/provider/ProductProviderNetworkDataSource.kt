package com.eahm.learn.business.data.network.abstraction.provider

import com.eahm.learn.business.domain.model.Product

interface ProductProviderNetworkDataSource {
    suspend fun insertProductProvider(providerId : String, productId : String) : Boolean
    suspend fun getAllProductsProviderIds(providerId: String) : List<Product>
    suspend fun deleteProductsProvider(providerId : String, productId: String) : Int
}