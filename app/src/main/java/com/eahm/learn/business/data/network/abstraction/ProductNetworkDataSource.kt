package com.eahm.learn.business.data.network.abstraction

import com.eahm.learn.business.domain.model.Product

interface ProductNetworkDataSource {
    suspend fun insertOrUpdateProduct(product : Product) : String
    suspend fun insertOrUpdateProducts(product : List<Product>)
    suspend fun insertDeletedProduct(product : Product)
    suspend fun insertDeletedProducts(products : List<Product>)

    suspend fun deleteProduct(id : String)
    suspend fun deleteDeletedProduct(product : Product)
    suspend fun getDeletedProducts() : List<Product>
    suspend fun deleteAllProducts()
    suspend fun searchProduct(product : Product) : Product?
    suspend fun getAllProducts() : List<Product>


}