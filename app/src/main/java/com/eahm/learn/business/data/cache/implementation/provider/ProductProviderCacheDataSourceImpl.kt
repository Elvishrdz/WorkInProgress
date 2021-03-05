package com.eahm.learn.business.data.cache.implementation.provider

import com.eahm.learn.business.data.cache.abstraction.provider.ProductProviderCacheDataSource
import com.eahm.learn.framework.datasource.cache.abstraction.provider.ProductProviderDaoService

class ProductProviderCacheDataSourceImpl(
        private val productProviderDaoService : ProductProviderDaoService
) : ProductProviderCacheDataSource {

    override suspend fun insertProductProvider(productId: String): Long {
        return productProviderDaoService.insertProductProvider(productId)
    }

    override suspend fun getAllProductsProviderIds(): List<String> {
        return productProviderDaoService.getAllProductsProviderIds()
    }

    override suspend fun deleteProductsProvider(): Int {
        return productProviderDaoService.deleteProductsProvider()
    }
}