package com.eahm.learn.framework.datasource.network.abstraction.provider

import com.eahm.learn.business.domain.model.Product

interface ProductProviderFirestoreService {
    suspend fun insertProductProvider(providerId : String, productId : String) : Boolean
    suspend fun getAllProductsProviderIds(providerId: String) : List<Product>
    suspend fun deleteProductProvider(providerId : String, productId: String) : Int
}