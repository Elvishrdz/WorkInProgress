package com.eahm.learn.business.data.cache.abstraction.provider

interface ProductProviderCacheDataSource {
    suspend fun insertProductProvider(productId : String) : Long
    suspend fun getAllProductsProviderIds() : List<String>
    suspend fun deleteProductsProvider() : Int
}