package com.eahm.learn.business.interactors.shoppingcart

import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.ShoppingCartCacheDataSource
import com.eahm.learn.business.data.network.abstraction.ShoppingCartNetworkDataSource
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.business.interactors.common.InsertProductInCart.Companion.SHOPPING_CART_LOCAL
import com.eahm.learn.framework.presentation.shoppingcart.state.ShoppingCartViewState
import com.eahm.learn.utils.logD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteItem (
    private val shoppingCartCacheDataSource: ShoppingCartCacheDataSource,
    private val shoppingCartNetworkDataSource: ShoppingCartNetworkDataSource
) {
    private val TAG = "DeleteItem"

    // TODO OPTIMIZE DELETE ITEM SOURCE CODE
    fun deleteItem(
            itemId : String,
            userId : String = SHOPPING_CART_LOCAL,
            stateEvent : StateEvent
    ) : Flow<DataState<ShoppingCartViewState>?> = flow{
        logD(TAG, "delete item: $itemId")

        if(userId.isNotEmpty() && userId != SHOPPING_CART_LOCAL){
            logD(TAG, "we are authenticated $userId")
            shoppingCartNetworkDataSource.removeFromShoppingCart(userId, itemId).let { serverResult ->
                if(serverResult > 0){
                    logD(TAG, "removed from server $serverResult")
                    shoppingCartCacheDataSource.removeFromShoppingCart(itemId).let { cacheResult ->
                        logD(TAG, "removed from cache $cacheResult")
                    }


                    val shoppingViewState = ShoppingCartViewState(
                            deletedItemID = itemId
                    )
                    emit(
                        DataState.data(
                                response = Response(
                                        message = SHOPPING_CART_DELETED_SUCCESS,
                                        uiComponentType = UIComponentType.Toast(),
                                        messageType = MessageType.Success()
                                ),
                                data = shoppingViewState,
                                stateEvent = stateEvent
                        )
                    )

                }
                else {
                    logD(TAG, "not deleted anything")
                    val shoppingViewState = ShoppingCartViewState(
                            deletedItemID = null
                    )

                    emit(
                        DataState.data(
                                response = Response(
                                        message = SHOPPING_CART_DELETED_FAIL,
                                        uiComponentType = UIComponentType.Toast(),
                                        messageType = MessageType.Error()
                                ),
                                data = shoppingViewState,
                                stateEvent = stateEvent
                        )
                    )
                }
            }

        }
        else {
            // local
            val cacheResult = safeCacheCall(IO){
                shoppingCartCacheDataSource.removeFromShoppingCart(itemId)
            }

            val cacheResponse = object : CacheResponseHandler<ShoppingCartViewState, Int>(
                response = cacheResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: Int): DataState<ShoppingCartViewState> {
                    return if(resultObj > 0){
                        val shoppingViewState = ShoppingCartViewState(
                            deletedItemID = itemId
                        )
                        DataState.data(
                                response = Response(
                                        message = SHOPPING_CART_DELETED_SUCCESS,
                                        uiComponentType = UIComponentType.Toast(),
                                        messageType = MessageType.Success()
                                ),
                                data = shoppingViewState,
                                stateEvent = stateEvent
                        )
                    }
                    else DataState.data(
                            response = Response(
                                    message = SHOPPING_CART_DELETED_FAIL,
                                    uiComponentType = UIComponentType.Toast(),
                                    messageType = MessageType.Error()
                            ),
                            data = null,
                            stateEvent = stateEvent
                    )
                }
            }

            emit(cacheResponse.getResult())
        }
    }

    companion object{
        const val SHOPPING_CART_DELETED_SUCCESS = "SUCCESSFULLY DELETED FROM CART"
        const val SHOPPING_CART_DELETED_FAIL = "FAILED DELETED FROM CART"
    }
}