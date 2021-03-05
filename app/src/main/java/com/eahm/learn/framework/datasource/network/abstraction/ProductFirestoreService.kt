package com.eahm.learn.framework.datasource.network.abstraction

import com.eahm.learn.business.domain.model.Product

interface ProductFirestoreService {

    suspend fun insertOrUpdateProduct(product: Product): String

    suspend fun insertOrUpdateProducts(products: List<Product>)

    suspend fun deleteProduct(primaryKey: String)

    suspend fun insertDeletedProduct(product: Product)

    suspend fun insertDeletedProducts(products: List<Product>)

    suspend fun deleteDeletedProduct(product: Product)

    suspend fun deleteAllProducts()

    suspend fun getDeletedProducts(): List<Product>

    suspend fun searchProduct(product: Product): Product?

    suspend fun getAllProducts(): List<Product>
}