package com.eahm.learn.business.data.cache.abstraction

import com.eahm.learn.business.domain.model.Product

interface ProductCacheDataSource {
    suspend fun insertProduct(newProduct : Product) : Long
    suspend fun insertProducts (newProducts : List<Product> ) : LongArray

    suspend fun deleteProduct(productId : String) : Int
    suspend fun deleteProducts(products: List<Product>): Int

    suspend fun searchProduct(id: String) : Product?
    suspend fun searchProducts(query: String, filter: String, order : String, page: Int): List<Product>

    suspend fun updateProduct(productId: String, newProductValues : Product, timestamp : String?) : Int

    suspend fun getNumProducts() : Int // Total number of products

}