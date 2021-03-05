package com.eahm.learn.business.data.network.implementation

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.data.network.abstraction.ProductNetworkDataSource
import com.eahm.learn.framework.datasource.network.abstraction.ProductFirestoreService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductNetworkDataSourceImpl
@Inject constructor(
    private val firestoreService : ProductFirestoreService
) : ProductNetworkDataSource {

    override suspend fun insertOrUpdateProduct(product: Product) : String =
        firestoreService.insertOrUpdateProduct(product)

    override suspend fun insertOrUpdateProducts(product: List<Product>) =
        firestoreService.insertOrUpdateProducts(product)

    override suspend fun deleteProduct(id: String) =
        firestoreService.deleteProduct(id)

    override suspend fun insertDeletedProduct(product: Product) =
        firestoreService.insertDeletedProduct(product)

    override suspend fun insertDeletedProducts(products: List<Product>) =
        firestoreService.insertDeletedProducts(products)

    override suspend fun deleteDeletedProduct(product: Product) =
        firestoreService.deleteDeletedProduct(product)

    override suspend fun getDeletedProducts(): List<Product> =
        firestoreService.getDeletedProducts()

    override suspend fun deleteAllProducts() =
        firestoreService.deleteAllProducts()

    override suspend fun searchProduct(product: Product): Product? =
        firestoreService.searchProduct(product)

    override suspend fun getAllProducts(): List<Product> =
        firestoreService.getAllProducts()

}
