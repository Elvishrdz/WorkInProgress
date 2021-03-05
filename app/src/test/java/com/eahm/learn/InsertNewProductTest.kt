package com.eahm.learn

import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.state.DataState
import com.eahm.learn.business.data.cache.CacheErrors
import com.eahm.learn.business.data.cache.abstraction.ProductCacheDataSource
import com.eahm.learn.business.data.network.FORCE_GENERAL_FAILURE
import com.eahm.learn.business.data.network.FORCE_NEW_PRODUCT_EXCEPTION
import com.eahm.learn.business.data.network.abstraction.ProductNetworkDataSource
import com.eahm.learn.business.interactors.productlist.InsertNewProduct
import com.eahm.learn.business.interactors.productlist.InsertNewProduct.Companion.INSERT_PRODUCT_SUCCESS
import com.eahm.learn.di.DependencyContainer
import com.eahm.learn.framework.presentation.productlist.state.ProductListViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import java.util.*
import com.eahm.learn.framework.presentation.productlist.state.ProductListStateEvent.*

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

/*
Test cases:
1. insertProduct_success_confirmNetworkAndCacheUpdated()
    a) insert a new Product
    b) listen for INSERT_Product_SUCCESS emission from flow
    c) confirm cache was updated with new Product
    d) confirm network was updated with new Product
2. insertProduct_fail_confirmNetworkAndCacheUnchanged()
    a) insert a new Product
    b) force a failure (return -1 from db operation)
    c) listen for INSERT_Product_FAILED emission from flow
    e) confirm cache was not updated
    e) confirm network was not updated
3. throwException_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) insert a new Product
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    e) confirm cache was not updated
    e) confirm network was not updated
 */

@InternalCoroutinesApi
class InsertNewProductTest {

    // system in test
    private val insertNewProduct: InsertNewProduct

    // dependencies
    private val dependencyContainer: DependencyContainer
    private val productCacheDataSource: ProductCacheDataSource
    private val productNetworkDataSource: ProductNetworkDataSource
    private val productFactory: ProductFactory

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        productCacheDataSource = dependencyContainer.ProductCacheDataSource
        productNetworkDataSource = dependencyContainer.ProductNetworkDataSource
        productFactory = dependencyContainer.ProductFactory
        insertNewProduct = InsertNewProduct(
            productCacheDataSource = productCacheDataSource,
            productNetworkDataSource = productNetworkDataSource,
            productFactory = productFactory
        )
    }

    @Test
    fun insertProduct_success_confirmNetworkAndCacheUpdated() = runBlocking {

        val newProduct = productFactory.createSingleProduct(
            id = UUID.randomUUID().toString(),
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString()
        )

        insertNewProduct.insertNewProduct(
            id = newProduct.id,
            title = newProduct.title,
            body = newProduct.description,
            stateEvent = InsertNewProductEvent(newProduct.title)
        ).collect(object: FlowCollector<DataState<ProductListViewState>?> {
            override suspend fun emit(value: DataState<ProductListViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    INSERT_PRODUCT_SUCCESS
                )
            }
        })

        // confirm network was updated
        val networkProductThatWasInserted = productNetworkDataSource.searchProduct(newProduct)
        assertTrue { networkProductThatWasInserted == newProduct }

        // confirm cache was updated
        val cacheProductThatWasInserted = productCacheDataSource.searchProduct(newProduct.id)
        assertTrue { cacheProductThatWasInserted == newProduct }
    }

    @Test
    fun insertProduct_fail_confirmNetworkAndCacheUnchanged() = runBlocking {

        val newProduct = productFactory.createSingleProduct(
            id = FORCE_GENERAL_FAILURE,
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString()
        )

        insertNewProduct.insertNewProduct(
            id = newProduct.id,
            title = newProduct.title,
            body = newProduct.description,
            stateEvent = InsertNewProductEvent(newProduct.title)
        ).collect(object: FlowCollector<DataState<ProductListViewState>?>{
            override suspend fun emit(value: DataState<ProductListViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    InsertNewProduct.INSERT_PRODUCT_FAILED
                )
            }
        })

        // confirm network was not changed
        val networkProductThatWasInserted = productNetworkDataSource.searchProduct(newProduct)
        assertTrue { networkProductThatWasInserted == null }

        // confirm cache was not changed
        val cacheProductThatWasInserted = productCacheDataSource.searchProduct(newProduct.id)
        assertTrue { cacheProductThatWasInserted == null }
    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        val newProduct = productFactory.createSingleProduct(
            id = FORCE_NEW_PRODUCT_EXCEPTION,
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString()
        )

        insertNewProduct.insertNewProduct(
            id = newProduct.id,
            title = newProduct.title,
            body = newProduct.description,
            stateEvent = InsertNewProductEvent(newProduct.title)
        ).collect(object: FlowCollector<DataState<ProductListViewState>?>{
            override suspend fun emit(value: DataState<ProductListViewState>?) {
                assert(
                    value?.stateMessage?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        // confirm network was not changed
        val networkProductThatWasInserted = productNetworkDataSource.searchProduct(newProduct)
        assertTrue { networkProductThatWasInserted == null }

        // confirm cache was not changed
        val cacheProductThatWasInserted = productCacheDataSource.searchProduct(newProduct.id)
        assertTrue { cacheProductThatWasInserted == null }
    }
}

























