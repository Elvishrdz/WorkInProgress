package com.eahm.learn.business.interactors.shoppingcart

import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.ShoppingCartCacheDataSource
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.framework.presentation.productdetail.state.ProductDetailStateEvent
import com.eahm.learn.framework.presentation.productdetail.state.ProductDetailViewState
import com.eahm.learn.utils.logD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchProductInCart<ViewState>(
    private val shoppingCartCacheDataSource: ShoppingCartCacheDataSource
) {

    private val TAG = "SearchProductInCart"

    fun searchProduct(
        shoppingCartID : String,
        stateEvent : StateEvent
    ) : Flow<DataState<ViewState>?> = flow{

        val result = safeCacheCall(IO){
            shoppingCartCacheDataSource.searchItem(shoppingCartID)
        }

        val response = object : CacheResponseHandler<ViewState, ShoppingCart?>(
                response = result,
                stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: ShoppingCart?): DataState<ViewState> {
                return if(resultObj != null){
                    when(stateEvent){
                        is ProductDetailStateEvent -> {
                            val response = ProductDetailViewState(
                                productInCart = resultObj
                            ) as ViewState

                            DataState.data(
                                response = Response(
                                    message = SHOPPING_CART_SEARCH_SUCCESS,
                                    uiComponentType = UIComponentType.Toast(),
                                    messageType = MessageType.Success()
                                ),
                                data = response,
                                stateEvent = stateEvent
                            )
                        }

                        else -> DataState.data(
                            response = Response(
                                message = SHOPPING_CART_SEARCH_FAILED,
                                uiComponentType = UIComponentType.None(),
                                messageType = MessageType.Error()
                            ),
                            data = null,
                            stateEvent = stateEvent
                        )
                    }
                }
                else {
                    DataState.error(
                        response = Response(
                            message = SHOPPING_CART_SEARCH_NOT_FOUNDED,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.None()
                        ),
                        stateEvent = stateEvent
                    )
                   /* DataState.error(
                        response = Response(
                            message = SHOPPING_CART_SEARCH_NOT_FOUNDED,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.None()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )*/
                }


            }

        }

        logD(TAG, "Return result: ${response.getResult()?.data.toString()} ${response.getResult()?.stateMessage?.response?.message}")
        emit(response.getResult())

    }

    companion object{
        const val SHOPPING_CART_SEARCH_SUCCESS = "Cart search successful!"
        const val SHOPPING_CART_SEARCH_FAILED = "Cart search has failed"
        const val SHOPPING_CART_SEARCH_NOT_FOUNDED = "The product was not founded"
    }
}