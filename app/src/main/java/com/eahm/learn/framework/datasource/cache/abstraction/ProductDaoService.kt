package com.eahm.learn.framework.datasource.cache.abstraction

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.framework.datasource.cache.dao.PRODUCT_PAGINATION_PAGE_SIZE

interface ProductDaoService {

    suspend fun insertProduct(product: Product): Long
    suspend fun insertProducts(products: List<Product>): LongArray

    suspend fun deleteProduct(id: String): Int
    suspend fun deleteProducts(products: List<Product>): Int

    suspend fun searchProduct(id: String): Product?
    suspend fun searchProducts(query: String, filter: String, order: String, page: Int) : List<Product>

    //region get products
    suspend fun getProducts(): List<Product>

    suspend fun getProductsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = PRODUCT_PAGINATION_PAGE_SIZE
    ): List<Product>

    suspend fun getProductsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = PRODUCT_PAGINATION_PAGE_SIZE
    ): List<Product>

    suspend fun getProductsOrderByTitleASC(
        query: String,
        page: Int,
        pageSize: Int = PRODUCT_PAGINATION_PAGE_SIZE
    ): List<Product>

    suspend fun getProductsOrderByTitleDESC(
        query: String,
        page: Int,
        pageSize: Int = PRODUCT_PAGINATION_PAGE_SIZE
    ): List<Product>
    //endregion get products

    suspend fun updateProduct(
        id: String,
        newProductValues : Product,
        timestamp : String?
    ): Int

    suspend fun getProductsTotalNum(): Int

}