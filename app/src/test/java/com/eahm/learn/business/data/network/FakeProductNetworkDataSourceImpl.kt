package com.eahm.learn.business.data.network

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.data.network.abstraction.ProductNetworkDataSource

class FakeProductNetworkDataSourceImpl
constructor(
    private val ProductsData: HashMap<String, Product>,
    private val deletedProductsData: HashMap<String, Product>
) : ProductNetworkDataSource {

    override suspend fun insertOrUpdateProduct(Product: Product) {
        ProductsData.put(Product.id, Product)
    }

    override suspend fun deleteProduct(primaryKey: String) {
        ProductsData.remove(primaryKey)
    }

    override suspend fun insertDeletedProduct(Product: Product) {
        deletedProductsData.put(Product.id, Product)
    }

    override suspend fun insertDeletedProducts(Products: List<Product>) {
        for(Product in Products){
            deletedProductsData.put(Product.id, Product)
        }
    }

    override suspend fun deleteDeletedProduct(Product: Product) {
        deletedProductsData.remove(Product.id)
    }

    override suspend fun getDeletedProducts(): List<Product> {
        return ArrayList(deletedProductsData.values)
    }

    override suspend fun deleteAllProducts() {
        deletedProductsData.clear()
    }

    override suspend fun searchProduct(Product: Product): Product? {
        return ProductsData.get(Product.id)
    }

    override suspend fun getAllProducts(): List<Product> {
        return ArrayList(ProductsData.values)
    }

    override suspend fun insertOrUpdateProducts(Products: List<Product>) {
        for(Product in Products){
            ProductsData.put(Product.id, Product)
        }
    }
}