package com.eahm.learn.business.interactors.productlist

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.ProductCacheDataSource
import com.eahm.learn.business.data.network.abstraction.ProductNetworkDataSource
import com.eahm.learn.business.data.utils.safeApiCall
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.framework.presentation.productlist.state.ProductListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteMultipleProducts(
    private val productCacheDataSource: ProductCacheDataSource,
    private val productNetworkDataSource: ProductNetworkDataSource
) {


    // set true if an error occurs when deleting any of the products from cache
    private var onDeleteError: Boolean = false

    /**
     * Logic:
     * 1. execute all the deletes and save result into an ArrayList<DataState<ProductListViewState>>
     * 2a. If one of the results is a failure, emit an "error" response
     * 2b. If all success, emit success response
     * 3. Update network with products that were successfully deleted
     */
    fun deleteProducts(
            products: List<Product>,
            stateEvent: StateEvent
    ): Flow<DataState<ProductListViewState>?> = flow {

        val successfulDeletes: ArrayList<Product> = ArrayList() // products that were successfully deleted
        for(product in products){
            val cacheResult = safeCacheCall(Dispatchers.IO){
                productCacheDataSource.deleteProduct(product.id)
            }

            val response = object: CacheResponseHandler<ProductListViewState, Int>(
                    response = cacheResult,
                    stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: Int): DataState<ProductListViewState>? {
                    if(resultObj < 0){ // if error
                        onDeleteError = true
                    }
                    else{
                        successfulDeletes.add(product)
                    }
                    return null
                }
            }.getResult()

            // check for random errors
            if(response?.stateMessage?.response?.message
                            ?.contains(stateEvent.errorInfo()) == true){
                onDeleteError = true
            }

        }

        if(onDeleteError){
            emit(
                    DataState.data<ProductListViewState>(
                            response = Response(
                                    message = DELETE_PRODUCTS_ERRORS,
                                    uiComponentType = UIComponentType.Dialog(),
                                    messageType = MessageType.Success()
                            ),
                            data = null,
                            stateEvent = stateEvent
                    )
            )
        }
        else{
            emit(
                    DataState.data<ProductListViewState>(
                            response = Response(
                                    message = DELETE_PRODUCTS_SUCCESS,
                                    uiComponentType = UIComponentType.Toast(),
                                    messageType = MessageType.Success()
                            ),
                            data = null,
                            stateEvent = stateEvent
                    )
            )
        }

        updateNetwork(successfulDeletes)
    }

    private suspend fun updateNetwork(successfulDeletes: ArrayList<Product>){
        for (product in successfulDeletes){

            // delete from "products" node
            safeApiCall(Dispatchers.IO){
                productNetworkDataSource.deleteProduct(product.id)
            }

            // insert into "deletes" node
            safeApiCall(Dispatchers.IO){
                productNetworkDataSource.insertDeletedProduct(product)
            }
        }
    }


    companion object{
        val DELETE_PRODUCTS_SUCCESS = "Successfully deleted products."
        val DELETE_PRODUCTS_ERRORS = "Not all the products you selected were deleted. There was some errors."
        val DELETE_PRODUCTS_YOU_MUST_SELECT = "You haven't selected any products to delete."
        val DELETE_PRODUCTS_ARE_YOU_SURE = "Are you sure you want to delete these?"
    }
}