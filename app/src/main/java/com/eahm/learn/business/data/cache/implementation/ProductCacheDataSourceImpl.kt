package com.eahm.learn.business.data.cache.implementation

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.data.cache.abstraction.ProductCacheDataSource
import com.eahm.learn.framework.datasource.cache.abstraction.ProductDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductCacheDataSourceImpl
@Inject constructor(
    private val productDaoService : ProductDaoService
) : ProductCacheDataSource {
//delegate all the functions to our service (our extra layer to test)

    override suspend fun insertProduct(newProduct: Product): Long =
        productDaoService.insertProduct(newProduct)

    override suspend fun insertProducts(newProducts: List<Product>): LongArray =
        productDaoService.insertProducts(newProducts)

    override suspend fun deleteProduct(productId: String): Int =
        productDaoService.deleteProduct(productId)

    override suspend fun deleteProducts(products: List<Product>): Int =
        productDaoService.deleteProducts(products)

    override suspend fun searchProduct(id: String) : Product? =
        productDaoService.searchProduct(id)

    override suspend fun searchProducts(query: String, filter: String, order : String, page: Int) : List<Product> =
        productDaoService.searchProducts(query, filter, order, page)

    override suspend fun updateProduct(productId: String, newProductValues: Product, timestamp : String?): Int =
        productDaoService.updateProduct(productId, newProductValues, timestamp)

    override suspend fun getNumProducts(): Int =
        productDaoService.getProductsTotalNum()

}
