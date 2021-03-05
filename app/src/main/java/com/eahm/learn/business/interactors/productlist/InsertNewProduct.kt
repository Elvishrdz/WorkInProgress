package com.eahm.learn.business.interactors.productlist

import com.eahm.learn.business.data.cache.abstraction.ProductCacheDataSource
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.data.network.ApiResponseHandler
import com.eahm.learn.business.data.network.abstraction.ProductNetworkDataSource
import com.eahm.learn.business.data.utils.safeApiCall
import com.eahm.learn.framework.presentation.product.state.ProductViewState
import com.eahm.learn.framework.presentation.productlist.state.ProductListViewState
import com.eahm.learn.utils.getFakeProductList
import com.eahm.learn.utils.logD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

// todo use a generic to provide a single way to execute (currently in productlist and product fragment)
class InsertNewProduct (
    private val productCacheDataSource: ProductCacheDataSource,
    private val productNetworkDataSource: ProductNetworkDataSource,
    private val productFactory: ProductFactory,
) {

    private val TAG = "InsertNewProduct"

    //todo fix this in only one method with generics
    fun insertNewProduct2(
        newProduct : Product,
        stateEvent: StateEvent
    ): Flow<DataState<ProductViewState>?> = flow {

      /*  val apiResult = safeApiCall(Main){
            productNetworkDataSource.insertOrUpdateProduct(newProduct)
        }

        val apiResponse = object: ApiResponseHandler<ProductViewState, Unit>(
            response = apiResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Unit): DataState<ProductViewState>? {
                val viewState = ProductViewState(
                    currentProduct = newProduct
                )

                return DataState.data(
                    response = Response(
                        message = INSERT_PRODUCT_SUCCESS,
                        uiComponentType = UIComponentType.Toast(),
                        messageType = MessageType.Success()
                    ),
                    data = viewState,
                    stateEvent = stateEvent
                )
            }


        }.getResult()

        emit(apiResponse)*/

        logD(TAG, "insert in server the product ${newProduct.title}")
        val productId = productNetworkDataSource.insertOrUpdateProduct(newProduct)

        if(productId.isEmpty()){
            logD(TAG, "not saved in network $productId")
            val viewState = ProductViewState(lastPublishedProduct = null)

            emit(
                    DataState.data(
                            response = Response(
                                    message = INSERT_PRODUCT_FAILED,
                                    uiComponentType = UIComponentType.None(),
                                    messageType = MessageType.Error()
                            ),
                            data = viewState,
                            stateEvent = stateEvent
                    )
            )
            return@flow
        }
        productCacheDataSource.insertProduct(
            productFactory.createSingleProduct(productId, newProduct)
        ).let {
            if(it > 0){
                logD(TAG, "product saved in cache $productId")
                val viewState = ProductViewState(lastPublishedProduct = newProduct)

                emit(
                    DataState.data(
                        response = Response(
                                message = INSERT_PRODUCT_SUCCESS,
                                uiComponentType = UIComponentType.Toast(),
                                messageType = MessageType.Success()
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )
                )
            }
            else {
                logD(TAG, "not saved in cache $productId")
                val viewState = ProductViewState(lastPublishedProduct = null)

                emit(
                    DataState.data(
                            response = Response(
                                    message = INSERT_PRODUCT_FAILED,
                                    uiComponentType = UIComponentType.None(),
                                    messageType = MessageType.Error()
                            ),
                            data = viewState,
                            stateEvent = stateEvent
                    )
                )
            }
        }
    }

    fun insertNewProduct(
            id: String? = null,
            title: String,
            body: String = "",
            stateEvent: StateEvent
    ): Flow<DataState<ProductListViewState>?> = flow {

        val list =  getFakeProductList()

        val newProduct = productFactory.createSingleProduct(
                id = id ?: UUID.randomUUID().toString(),
                list[0]
        )

        val apiResult = safeApiCall(IO){
            productNetworkDataSource.insertOrUpdateProduct(newProduct)
        }

        val apiResponse = object: ApiResponseHandler<ProductListViewState, String>(
                response = apiResult,
                stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: String): DataState<ProductListViewState>? {
                val viewState =
                        ProductListViewState(
                                newProduct = productFactory.createSingleProduct(resultObj, newProduct)
                        )
                return DataState.data(
                        response = Response(
                                message = INSERT_PRODUCT_SUCCESS,
                                uiComponentType = UIComponentType.Toast(),
                                messageType = MessageType.Success()
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                )
            }


        }.getResult()

        emit(apiResponse)

    }

    companion object{
        const val INSERT_PRODUCT_SUCCESS = "Product successfully inserted"
        const val INSERT_PRODUCT_FAILED = "Failed to insert product"
    }
}

