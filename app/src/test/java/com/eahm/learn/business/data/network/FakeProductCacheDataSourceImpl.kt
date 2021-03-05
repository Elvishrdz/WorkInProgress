package com.eahm.learn.business.data.network

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.util.DateUtil
import com.eahm.learn.business.data.cache.abstraction.ProductCacheDataSource
import com.eahm.learn.framework.datasource.cache.dao.PRODUCT_PAGINATION_PAGE_SIZE

const val FORCE_DELETE_PRODUCT_EXCEPTION = "FORCE_DELETE_Product_EXCEPTION"
const val FORCE_DELETES_PRODUCT_EXCEPTION = "FORCE_DELETES_Product_EXCEPTION"
const val FORCE_UPDATE_PRODUCT_EXCEPTION = "FORCE_UPDATE_Product_EXCEPTION"
const val FORCE_NEW_PRODUCT_EXCEPTION = "FORCE_NEW_Product_EXCEPTION"
const val FORCE_SEARCH_PRODUCT_EXCEPTION = "FORCE_SEARCH_ProductS_EXCEPTION"
const val FORCE_GENERAL_FAILURE = "FORCE_GENERAL_FAILURE"

class FakeProductCacheDataSourceImpl constructor(
    private val ProductsData: HashMap<String, Product>,
    private val dateUtil: DateUtil
): ProductCacheDataSource {

    override suspend fun insertProduct(Product: Product): Long {
        if(Product.id.equals(FORCE_NEW_PRODUCT_EXCEPTION)){
            throw Exception("Something went wrong inserting the Product.")
        }
        if(Product.id.equals(FORCE_GENERAL_FAILURE)){
            return -1 // fail
        }
        ProductsData.put(Product.id, Product)
        return 1 // success
    }

    override suspend fun deleteProduct(primaryKey: String): Int {
        if(primaryKey.equals(FORCE_DELETE_PRODUCT_EXCEPTION)){
            throw Exception("Something went wrong deleting the Product.")
        }
        else if(primaryKey.equals(FORCE_DELETES_PRODUCT_EXCEPTION)){
            throw Exception("Something went wrong deleting the Product.")
        }
        return ProductsData.remove(primaryKey)?.let {
            1 // return 1 for success
        }?: - 1 // -1 for failure
    }

    override suspend fun deleteProducts(Products: List<Product>): Int {
        var failOrSuccess = 1
        for(Product in Products){
            if(ProductsData.remove(Product.id) == null){
                failOrSuccess = -1 // mark for failure
            }
        }
        return failOrSuccess
    }

    override suspend fun updateProduct(
        productId: String,
        newProductValues :Product,
        timestamp : String?
    ): Int {
        if(productId.equals(FORCE_UPDATE_PRODUCT_EXCEPTION)){
            throw Exception("Something went wrong updating the Product.")
        }
        val updatedProduct = Product(
            id = productId,
            title = newProductValues.title,
            description = newProductValues.description?: "",
            updated_at = dateUtil.getCurrentTimestamp(),
            created_at = ProductsData.get(productId)?.created_at?: dateUtil.getCurrentTimestamp()
        )
        return ProductsData.get(productId)?.let {
            ProductsData.put(productId, updatedProduct)
            1 // success
        }?: -1 // nothing to update
    }

    // Not testing the order/filter. Just basic query
    // simulate SQLite "LIKE" query on title and body
    override suspend fun searchProducts(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Product> {
        if(query.equals(FORCE_SEARCH_PRODUCT_EXCEPTION)){
            throw Exception("Something went searching the cache for Products.")
        }
        val results: ArrayList<Product> = ArrayList()
        for(Product in ProductsData.values){
            if(Product.title.contains(query)){
                results.add(Product)
            }
            else if(Product.description.contains(query)){
                results.add(Product)
            }
            if(results.size > (page * PRODUCT_PAGINATION_PAGE_SIZE)){
                break
            }
        }
        return results
    }

    override suspend fun searchProduct(id: String): Product? {
        return ProductsData.get(id)
    }

    override suspend fun getNumProducts(): Int {
        return ProductsData.size
    }

    override suspend fun insertProducts(Products: List<Product>): LongArray {
        val results = LongArray(Products.size)
        for((index,Product) in Products.withIndex()){
            results[index] = 1
            ProductsData.put(Product.id, Product)
        }
        return results
    }
}