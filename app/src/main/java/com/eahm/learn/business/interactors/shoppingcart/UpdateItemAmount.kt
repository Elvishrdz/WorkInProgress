package com.eahm.learn.business.interactors.shoppingcart

import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.ShoppingCartCacheDataSource
import com.eahm.learn.business.data.network.abstraction.ShoppingCartNetworkDataSource
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.business.interactors.common.InsertProductInCart
import com.eahm.learn.business.interactors.common.InsertProductInCart.Companion.SHOPPING_CART_LOCAL
import com.eahm.learn.framework.presentation.shoppingcart.state.ShoppingCartViewState
import com.eahm.learn.utils.logD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateItemAmount(
    private val shoppingCartCacheDataSource: ShoppingCartCacheDataSource,
    private val shoppingCartNetworkDataSource: ShoppingCartNetworkDataSource
) {

    private val TAG = "UpdateItemAmount"

    // TODO OPTIMIZE UPDATE ITEM AMOUNT SOURCE CODE
    fun updateItemAmount(
        shoppingCartToUpdate : ShoppingCart,
        newAmount : Int,
        userId : String = SHOPPING_CART_LOCAL,
        stateEvent : StateEvent
    ) : Flow<DataState<ShoppingCartViewState>?> = flow {

        logD(TAG, "UpdateItemAmount ${shoppingCartToUpdate.product.title}")

        if(userId.isNotEmpty() && userId != SHOPPING_CART_LOCAL){
            logD(TAG, "we are authenticated $userId")

            shoppingCartNetworkDataSource.updateAmount(
                    userId = userId,
                    shoppingCartId = shoppingCartToUpdate.id,
                    newAmount = newAmount
            ).let { serverResult ->
                if(serverResult > 0){
                    logD(TAG, "updated in server $serverResult")

                    shoppingCartCacheDataSource.updateAmount(
                            shoppingCartId = shoppingCartToUpdate.id,
                            newAmount = newAmount
                    ).let { cacheResult ->
                        logD(TAG, "updated in cache $cacheResult")
                    }

                    val dataStateEvent = ShoppingCartViewState(
                            updatedItem = ShoppingCart(
                                    id = shoppingCartToUpdate.id,
                                    product = shoppingCartToUpdate.product,
                                    amount = newAmount)
                    )

                    emit(
                            DataState.data(
                                    response = Response(
                                            message = SHOPPING_CART_UPDATE_ITEM_AMOUNT_SUCCESS,
                                            uiComponentType = UIComponentType.Toast(),
                                            messageType = MessageType.Success()
                                    ),
                                    data = dataStateEvent,
                                    stateEvent = stateEvent
                            )
                    )

                }
                else {
                    val dataStateEvent = ShoppingCartViewState(
                            updatedItem = null
                    )

                    emit(
                        DataState.data(
                                response = Response(
                                        message = SHOPPING_CART_UPDATE_ITEM_AMOUNT_FAILED,
                                        uiComponentType = UIComponentType.Toast(),
                                        messageType = MessageType.Error()
                                ),
                                data = dataStateEvent,
                                stateEvent = stateEvent
                        )
                    )
                }
            }

        }
        else {
            val cacheResponse = safeCacheCall(IO){
                shoppingCartCacheDataSource.updateAmount(
                        shoppingCartId = shoppingCartToUpdate.id,
                        newAmount = newAmount)
            }

            logD(TAG, "executed! $newAmount")

            val cacheResult = object : CacheResponseHandler<ShoppingCartViewState, Int>(
                    response = cacheResponse,
                    stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: Int): DataState<ShoppingCartViewState> {
                    logD(TAG, "executed! $resultObj")

                    return if(resultObj > 0){
                        val dataStateEvent = ShoppingCartViewState(
                                updatedItem = ShoppingCart(
                                        id = shoppingCartToUpdate.id,
                                        product = shoppingCartToUpdate.product,
                                        amount = newAmount)
                        )

                        DataState.data(
                                response = Response(
                                        message = SHOPPING_CART_UPDATE_ITEM_AMOUNT_SUCCESS,
                                        uiComponentType = UIComponentType.Toast(),
                                        messageType = MessageType.Success()
                                ),
                                data = dataStateEvent,
                                stateEvent = stateEvent
                        )
                    }
                    else DataState.data(
                            response = Response(
                                    message = SHOPPING_CART_UPDATE_ITEM_AMOUNT_FAILED,
                                    uiComponentType = UIComponentType.Toast(),
                                    messageType = MessageType.Error()
                            ),
                            data = null,
                            stateEvent = stateEvent
                    )
                }
            }

            emit(cacheResult.getResult())
        }

    }

    companion object {
        const val SHOPPING_CART_UPDATE_ITEM_AMOUNT_SUCCESS = "Product amount successfully Updated!"
        const val SHOPPING_CART_UPDATE_ITEM_AMOUNT_FAILED = "Failed updating product amount!"
    }
}