package com.eahm.learn.business.interactors.productlist

import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.ProductCacheDataSource
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.framework.presentation.productlist.state.ProductListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNumProducts(
        private val productCacheDataSource: ProductCacheDataSource
){
    fun getNumProducts(
            stateEvent: StateEvent
    ): Flow<DataState<ProductListViewState>?> = flow {



        val cacheResult = safeCacheCall(Dispatchers.IO){
            productCacheDataSource.getNumProducts()
        }

        val response =  object: CacheResponseHandler<ProductListViewState, Int>(
                response = cacheResult,
                stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ProductListViewState>? {
                val viewState = ProductListViewState(
                        numProductsInCache = resultObj
                )
                return DataState.data(
                        response = Response(
                                message = GET_NUM_PRODUCTS_SUCCESS,
                                uiComponentType = UIComponentType.None(),
                                messageType = MessageType.Success()
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object{
        val GET_NUM_PRODUCTS_SUCCESS = "Successfully retrieved the number of products from the cache."
        val GET_NUM_PRODUCTS_FAILED = "Failed to get the number of products from the cache."
    }

}