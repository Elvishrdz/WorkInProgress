package com.eahm.learn.business.interactors.shoppingcart.order

import com.eahm.learn.business.data.cache.abstraction.OrderCacheDataSource
import com.eahm.learn.business.data.cache.abstraction.ShoppingCartCacheDataSource
import com.eahm.learn.business.data.network.ApiResponseHandler
import com.eahm.learn.business.data.network.abstraction.OrderNetworkDataSource
import com.eahm.learn.business.data.network.abstraction.ShoppingCartNetworkDataSource
import com.eahm.learn.business.data.utils.safeApiCall
import com.eahm.learn.business.domain.model.Order
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.framework.presentation.shoppingcart.state.ShoppingCartViewState
import com.eahm.learn.utils.logD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddNewOrder(
    private val orderNetworkDataSource: OrderNetworkDataSource,
    private val orderCacheDataSource: OrderCacheDataSource,
    private val shoppingCartCacheDataSource: ShoppingCartCacheDataSource,
    private val shoppingCartNetworkDataSource: ShoppingCartNetworkDataSource
) {
    private val TAG = "AddNewOrder"

    fun addNewOrder(
            newOrder : Order,
            stateEvent: StateEvent
    ) : Flow<DataState<ShoppingCartViewState>?> = flow {

        val orderId =  orderNetworkDataSource.placeOrder(newOrder)

        if(orderId.isNotEmpty()){
            // clean our shopping cart
            shoppingCartNetworkDataSource.cleanShoppingCart(newOrder.clientId).let {
                logD(TAG, "clean ${newOrder.clientId} shopping cart in server $it")
            }

            shoppingCartCacheDataSource.cleanShoppingCart(isLocal = false).let {
                logD(TAG, "clean our shopping cart in cache $it")
            }

            // save order in cache
            orderCacheDataSource.insertOrder(newOrder).let {
                logD(TAG, "order saved in cache $it")
            }

            // all done
            emit(
                DataState.data(
                        response = Response (
                                message = ORDER_ADD_SUCCESS,
                                uiComponentType = UIComponentType.None(),
                                messageType = MessageType.Success()
                        ),
                        data = ShoppingCartViewState(
                                myOrderId = orderId,
                                itemsList = mutableListOf() // the list must be clean, notify that
                        ),
                        stateEvent = stateEvent
                )
            )

        }
        else {
            // something fail
            emit(
                DataState.data(
                        response = Response (
                                message = ORDER_ADD_FAIL,
                                uiComponentType = UIComponentType.None(),
                                messageType = MessageType.Error()
                        ),
                        data = ShoppingCartViewState(
                                myOrderId = null
                        ),
                        stateEvent = stateEvent
                )
            )
        }

       /* val response = safeApiCall(IO){
            orderNetworkDataSource.placeOrder(newOrder)
        }

        val result = object : ApiResponseHandler<ShoppingCartViewState, String?>(
            response = response,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: String?): DataState<ShoppingCartViewState>? {
                return if(resultObj != null && resultObj.isNotEmpty()){
                    DataState.data(
                        response = Response (
                            message = ORDER_ADD_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        data = ShoppingCartViewState(
                            myOrderId = resultObj
                        ),
                        stateEvent = stateEvent
                    )
                }
                else DataState.data(
                    response = Response (
                        message = ORDER_ADD_FAIL,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Error()
                    ),
                    data = null,
                    stateEvent = stateEvent
                )
            }
        }

        emit(result.getResult())*/
    }

    companion object {
        const val ORDER_ADD_SUCCESS = "The order is placed!"
        const val ORDER_ADD_FAIL = "The order was NOT placed."
    }

}