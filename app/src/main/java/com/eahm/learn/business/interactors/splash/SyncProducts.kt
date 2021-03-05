package com.eahm.learn.business.interactors.splash

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.state.DataState
import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.ProductCacheDataSource
import com.eahm.learn.business.data.network.ApiResponseHandler
import com.eahm.learn.business.data.network.abstraction.ProductNetworkDataSource
import com.eahm.learn.business.data.network.abstraction.ProviderNetworkDataSource
import com.eahm.learn.business.data.utils.safeApiCall
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.state.StateEvent
import com.eahm.learn.business.interactors.common.sync.SyncProvider
import com.eahm.learn.framework.datasource.cache.dao.ORDER_BY_ALL
import com.eahm.learn.framework.datasource.cache.dao.PRODUCT_DEFAULT_PAGE
import com.eahm.learn.framework.datasource.cache.dao.PRODUCT_DEFAULT_QUERY
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect

class SyncProducts(
    private val productCacheDataSource: ProductCacheDataSource,
    private val productNetworkDataSource: ProductNetworkDataSource,
    private val productFactory: ProductFactory,
    private val providerNetworkDataSource: ProviderNetworkDataSource,
    private val syncProvider: SyncProvider
) {

    suspend fun syncProducts() {
        // Obtain all the products from the server
        val networkProductList = getNetworkProducts()

        // Obtain product providers
        val productList = getProductProviders(networkProductList)

        // Obtain all the products availables from the database
        val cachedProductsList = getCachedProducts()

        // Sync local and remote data
        syncNetworkProductsWithCachedProducts(
                ArrayList(cachedProductsList),
                networkProductList
        )

    }

    private suspend fun getCachedProducts(): List<Product> {
        val cacheResult = safeCacheCall(IO){
            productCacheDataSource.searchProducts(
                query = PRODUCT_DEFAULT_QUERY,
                filter = ORDER_BY_ALL,
                order = ORDER_BY_ALL,
                page = PRODUCT_DEFAULT_PAGE
            )
        }

        val response = object :CacheResponseHandler<List<Product>, List<Product>>(
                response = cacheResult,
                stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<Product>): DataState<List<Product>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        return response?.data ?: ArrayList()

    }

    private suspend fun getNetworkProducts(): List<Product> {

        val networkResponse = safeApiCall(IO){
            productNetworkDataSource.getAllProducts()
        }

        val response = object : ApiResponseHandler<List<Product>, List<Product>>(
                response = networkResponse,
                stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<Product>): DataState<List<Product>>? {
                return DataState.data(
                        response = null,
                        data = resultObj,
                        stateEvent = null
                )
            }

        }.getResult()

//        var list: List<Product> = getFakeProductList()
//
//        response?.data?.let{
//            if(it.isNotEmpty()) list = it
//        }
//
//        logList("CLOUDFIRES", list)
//
//        return list
        return response?.data ?: listOf()

    }

    private suspend fun getProductProviders(networkProductList: List<Product>): List<Product> {

       /* return networkProductList.map {
            val providerData = getProvider(it.id)
            productFactory.createProduct(product = it, provider = providerData)
        } */
        val newList : MutableList<Product> = mutableListOf()

        for(product in networkProductList){

            syncProvider.syncProvider(product.id, null)
                    .collect {  productViewState ->

                        val providerData = productViewState.data?.provider
                        providerData?.let { _ ->

                            newList.add(
                                productFactory.createProduct(product = product, provider = providerData)
                            )
                        }
                    }
        }

        return newList
    }

    private suspend fun getProvider(id: String): Provider {
        return providerNetworkDataSource.getProvider(id) ?: Provider(id, null, null)
    }


    private suspend fun syncNetworkProductsWithCachedProducts(
        cachedProducts: ArrayList<Product>,
        networkProducts: List<Product>
    ) {

        for(serverProduct in networkProducts){
            // Search the product in the database if doesnt exist, then add it
            productCacheDataSource.searchProduct(serverProduct.id)?.let {
                cachedProducts.remove(serverProduct)
                productCacheDataSource.updateProduct(it.id, it, it.updated_at)
                //checkIfCachedProductRequiresUpdate(serverProduct, serverProduct)
            } ?: productCacheDataSource.insertProduct(serverProduct)
        }

        // Removed products unused and old cached products
        for(oldProduct in cachedProducts){
            productCacheDataSource.deleteProduct(oldProduct.id)
        }

    }

    private suspend fun checkIfCachedProductRequiresUpdate(cachedProduct: Product, networkProduct: Product) {
        // Update products if they have a recent updated_at value

        val cacheUpdatedAt = cachedProduct.updated_at
        val networkUpdatedAt = networkProduct.updated_at

        if(cacheUpdatedAt < networkUpdatedAt){
            // Update cached product. It is out of date.
            productCacheDataSource.updateProduct(
                    networkProduct.id,
                    networkProduct,
                    networkProduct.updated_at
            )
        }
        // ELSE: The product has no update

    }
}