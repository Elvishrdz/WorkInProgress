package com.eahm.learn.framework.datasource.cache.implementation

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.util.DateUtil
import com.eahm.learn.framework.datasource.cache.abstraction.ProductDaoService
import com.eahm.learn.framework.datasource.cache.dao.ProductDao
import com.eahm.learn.framework.datasource.cache.dao.searchProducts
import com.eahm.learn.framework.datasource.cache.mappers.ProductCacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductDaoServiceImpl
@Inject
constructor(
    private val productDao: ProductDao,
    private val productMapper: ProductCacheMapper,
    private val dateUtil: DateUtil
): ProductDaoService {

    override suspend fun insertProduct(product: Product): Long {
        return productDao.insertProduct(productMapper.mapToEntity(product))
    }

    override suspend fun insertProducts(products: List<Product>): LongArray {
        return productDao.insertProducts(
            productMapper.productListToEntityList(products)
        )
    }

    override suspend fun deleteProduct(id: String): Int {
        return productDao.deleteProduct(id)
    }

    override suspend fun deleteProducts(products: List<Product>): Int {
        val ids = products.mapIndexed {index, value -> value.id}
        return productDao.deleteProducts(ids)
    }

    override suspend fun searchProduct(id: String): Product? {
        return productDao.searchProduct(id)?.let { product ->
            productMapper.mapFromEntity(product)
        }
    }

    override suspend fun searchProducts(
        query: String,
        filter: String,
        order: String,
        page: Int
    ): List<Product> {
        return productMapper.entityListToProductList(
            productDao.searchProducts(
                query = query,
                filter = filter,
                order = order,
                page = page
            )
        )
    }

    //region get products

    override suspend fun getProducts(): List<Product> {
        return productMapper.entityListToProductList(productDao.getProducts())
    }

    override suspend fun getProductsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Product> {
        return productMapper.entityListToProductList(
            productDao.getProductsOrderByDateDESC(
                query = query,
                page = page,
                pageSize = pageSize
            )
        )
    }

    override suspend fun getProductsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Product> {
        return productMapper.entityListToProductList(
            productDao.getProductsOrderByDateASC(
                query = query,
                page = page,
                pageSize = pageSize
            )
        )
    }

    override suspend fun getProductsOrderByTitleDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Product> {
        return productMapper.entityListToProductList(
            productDao.getProductsOrderByTitleDESC(
                query = query,
                page = page,
                pageSize = pageSize
            )
        )
    }

    override suspend fun getProductsOrderByTitleASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Product> {
        return productMapper.entityListToProductList(
            productDao.getProductsOrderByTitleASC(
                query = query,
                page = page,
                pageSize = pageSize
            )
        )
    }
    //endregion get products

    override suspend fun updateProduct(
        id: String,
        newProductValues: Product,
        timestamp: String?
    ): Int {
        return if(timestamp != null){
            productDao.updateProduct(
                primaryKey = id,
                title = newProductValues.title,
                description = newProductValues.description,
                updated_at = timestamp
            )
        }
        else{
            productDao.updateProduct(
                primaryKey = id,
                title = newProductValues.title,
                description = newProductValues.description,
                updated_at = dateUtil.getCurrentTimestamp()
            )
        }
    }

    override suspend fun getProductsTotalNum(): Int {
        return productDao.getNumProducts()
    }

}