package com.eahm.learn.framework.datasource.cache.abstraction.provider

interface ProductProviderDaoService {
    suspend fun insertProductProvider(productId : String) : Long
    suspend fun getAllProductsProviderIds() : List<String>
    suspend fun deleteProductsProvider() : Int
}