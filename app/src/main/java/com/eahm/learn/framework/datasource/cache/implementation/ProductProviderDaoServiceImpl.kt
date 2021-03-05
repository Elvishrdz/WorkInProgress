package com.eahm.learn.framework.datasource.cache.implementation

import com.eahm.learn.framework.datasource.cache.abstraction.provider.ProductProviderDaoService
import com.eahm.learn.framework.datasource.cache.dao.provider.ProductProviderDao
import com.eahm.learn.framework.datasource.cache.mappers.provider.ProductProviderCacheMapper

class ProductProviderDaoServiceImpl(
        private val productProviderDao : ProductProviderDao,
        private val productProviderCacheMapper : ProductProviderCacheMapper
): ProductProviderDaoService {

    override suspend fun insertProductProvider(productId: String): Long {
        return productProviderDao.insertProductProvider(
                productProviderCacheMapper.mapToEntity(productId)
        )
    }

    override suspend fun getAllProductsProviderIds(): List<String> {
        return productProviderDao.getAllProductsProvider()?.let {
            productProviderCacheMapper.mapFromEntityList(it)
        } ?: listOf()
    }

    override suspend fun deleteProductsProvider(): Int {
        return productProviderDao.deleteProductsProvider()
    }
}