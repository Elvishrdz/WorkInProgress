package com.eahm.learn.business.interactors.common

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.ProductCacheDataSource
import com.eahm.learn.business.data.network.abstraction.ProductNetworkDataSource
import com.eahm.learn.business.data.utils.safeCacheCall
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteProduct<ViewState>(
    private val productCacheDataSource: ProductCacheDataSource,
    private val productNetworkDataSource: ProductNetworkDataSource
){

    fun deleteProduct(
        product: Product,
        stateEvent: StateEvent
    ): Flow<DataState<ViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            productCacheDataSource.deleteProduct(product.id)
        }

        val response = object: CacheResponseHandler<ViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ViewState>? {
                return if(resultObj > 0){
                    DataState.data(
                        response = Response(
                            message = DELETE_PRODUCT_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
                else{
                    DataState.data(
                        response = Response(
                            message = DELETE_PRODUCT_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }
        }

        emit(response.getResult())
    }
    
    companion object{
        const val DELETE_PRODUCT_SUCCESS = "Successfully deleted product."
        const val DELETE_PRODUCT_FAILED = "Failed to delete product."
    }
}



