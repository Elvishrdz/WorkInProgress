package com.eahm.learn.business.interactors.common

import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.ShoppingCartCacheDataSource
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.framework.presentation.productlist.state.ProductListStateEvent
import com.eahm.learn.framework.presentation.productlist.state.ProductListViewState
import com.eahm.learn.framework.presentation.shoppingcart.state.ShoppingCartStateEvent
import com.eahm.learn.framework.presentation.shoppingcart.state.ShoppingCartViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetShoppingCartItemAmountNum<ViewState> (
    private val shoppingCartCacheDataSource : ShoppingCartCacheDataSource
) {

    fun getShoppingCartItemAmountNum(
        isLocal : Boolean,
        stateEvent : StateEvent
    ): Flow<DataState<ViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            shoppingCartCacheDataSource.getItemsAmount(isLocal)
        }

        val cacheResponse = object : CacheResponseHandler<ViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ViewState> {
                return when (stateEvent) {
                    is ProductListStateEvent -> {
                        val data = ProductListViewState(
                                numItemsInCart = resultObj
                        )  as ViewState
                        DataState.data(
                                response = Response(
                                        message = SHOPPING_CART_GET_ITEM_SUCCESS,
                                        uiComponentType = UIComponentType.None(),
                                        messageType = MessageType.Success()
                                ),
                                data = data,
                                stateEvent = stateEvent
                        )
                    }
                    is ShoppingCartStateEvent -> {
                        val data = ShoppingCartViewState(
                                numItemInCache = resultObj
                        ) as ViewState
                        DataState.data(
                                response = Response(
                                        message = SHOPPING_CART_GET_ITEM_SUCCESS,
                                        uiComponentType = UIComponentType.None(),
                                        messageType = MessageType.Success()
                                ),
                                data = data,
                                stateEvent = stateEvent
                        )
                    }
                    else -> DataState.data(
                            response = Response(
                                    message = SHOPPING_CART_GET_ITEM_FAILED,
                                    uiComponentType = UIComponentType.None(),
                                    messageType = MessageType.Error()
                            ),
                            data = null,
                            stateEvent = stateEvent
                    )
                }
            }
        }

        emit(cacheResponse.getResult())

    }

    companion object{
        const val SHOPPING_CART_GET_ITEM_SUCCESS = "Successfully obtained the items in the shopping cart"
        const val SHOPPING_CART_GET_ITEM_FAILED = "Failed obtaining the items in the shopping cart"
    }
}