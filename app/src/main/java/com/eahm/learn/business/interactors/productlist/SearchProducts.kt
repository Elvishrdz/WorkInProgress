package com.eahm.learn.business.interactors.productlist

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.ProductCacheDataSource
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.framework.datasource.cache.dao.ORDER_BY_ALL
import com.eahm.learn.framework.datasource.cache.dao.PRODUCT_DEFAULT_PAGE
import com.eahm.learn.framework.datasource.cache.dao.PRODUCT_DEFAULT_QUERY
import com.eahm.learn.framework.presentation.productlist.state.ProductListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchProducts(
    private val productCacheDataSource: ProductCacheDataSource
){

    fun searchProducts(
        query: String = PRODUCT_DEFAULT_QUERY,
        filter: String = ORDER_BY_ALL,
        order: String = ORDER_BY_ALL,
        page: Int = PRODUCT_DEFAULT_PAGE,
        stateEvent: StateEvent
    ): Flow<DataState<ProductListViewState>?> = flow {

        var updatedPage = page
        if(page <= 0){
            updatedPage = 1
        }
        val cacheResult = safeCacheCall(Dispatchers.IO){
            productCacheDataSource.searchProducts(
                query = query,
                filter = filter,
                order = order,
                page = updatedPage
            )
        }

        val response = object: CacheResponseHandler<ProductListViewState, List<Product>>(
                response = cacheResult,
                stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: List<Product>): DataState<ProductListViewState>? {
                var message: String? = SEARCH_PRODUCTS_SUCCESS
                var uiComponentType: UIComponentType? = UIComponentType.None()
                if(resultObj.isEmpty()){
                    message = SEARCH_PRODUCTS_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast()
                }
                return DataState.data(
                        response = Response(
                                message = message,
                                uiComponentType = uiComponentType as UIComponentType,
                                messageType = MessageType.Success()
                        ),
                        data = ProductListViewState(
                                productList = ArrayList(resultObj)
                        ),
                        stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object{
        val SEARCH_PRODUCTS_SUCCESS = "Successfully retrieved list of products."
        val SEARCH_PRODUCTS_NO_MATCHING_RESULTS = "There are no products that match that query."
        val SEARCH_PRODUCTS_FAILED = "Failed to retrieve the list of products."

    }
}

