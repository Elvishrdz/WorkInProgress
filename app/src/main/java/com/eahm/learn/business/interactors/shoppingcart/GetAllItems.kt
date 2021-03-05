package com.eahm.learn.business.interactors.shoppingcart

import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.ShoppingCartCacheDataSource
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.framework.presentation.shoppingcart.state.ShoppingCartViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAllItems(
    private val shoppingCartCacheDataSource: ShoppingCartCacheDataSource
) {

    fun getAllItems(
        isLocal : Boolean,
        stateEvent : StateEvent
    ) : Flow<DataState<ShoppingCartViewState>?> = flow{

        val cacheResult = safeCacheCall(IO){
            shoppingCartCacheDataSource.getAllShoppingCart(isLocal)
        }

        val cacheResponse = object : CacheResponseHandler<ShoppingCartViewState, List<ShoppingCart>>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: List<ShoppingCart>): DataState<ShoppingCartViewState> {

                var response = Response(
                    message = SHOPPING_CART_GET_ALL_FAILED,
                    uiComponentType = UIComponentType.Toast(),
                    messageType = MessageType.Error()
                )
                var data : ShoppingCartViewState? = null

                if(resultObj.isNotEmpty()){
                    response = Response(
                        message = SHOPPING_CART_GET_ALL_SUCCESS,
                        uiComponentType = UIComponentType.Toast(),
                        messageType = MessageType.Success()
                    )
                    data = ShoppingCartViewState(
                        itemsList = resultObj.toMutableList()
                    )
                }

                return DataState.data(response, data, stateEvent)
            }
        }

        emit(cacheResponse.getResult())

    }

    companion object{
        const val SHOPPING_CART_GET_ALL_SUCCESS = "Shopping cart items successfully retrieved"
        const val SHOPPING_CART_GET_ALL_FAILED = "Failed to obtain items in shopping cart"
    }

}