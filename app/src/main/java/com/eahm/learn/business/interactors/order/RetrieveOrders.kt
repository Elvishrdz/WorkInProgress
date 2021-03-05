package com.eahm.learn.business.interactors.order

import com.eahm.learn.business.data.cache.abstraction.OrderCacheDataSource
import com.eahm.learn.business.data.network.abstraction.OrderNetworkDataSource
import com.eahm.learn.business.domain.model.Order
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.framework.presentation.orderlist.state.OrderListViewState
import com.eahm.learn.utils.logD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RetrieveOrders
@Inject constructor(
    private val orderCacheDataSource: OrderCacheDataSource,
    private val orderNetworkDataSource: OrderNetworkDataSource,
){
    private val TAG = "RetrieveOrders"

    fun retrieveOrders(
        userId : String,
        stateEvent : StateEvent
    ) : Flow<DataState<OrderListViewState>> = flow {

        val orders : MutableList<Order> = mutableListOf()

        if(userId.isEmpty()){
            logD(TAG, "the user id is empty")
            emit(
                    DataState.data(
                            response = Response(
                                    message = "Not Authenticated",
                                    uiComponentType = UIComponentType.None(),
                                    messageType = MessageType.None()
                            ),
                            data =  OrderListViewState(
                                    orderList = null
                            ),
                            stateEvent = stateEvent
                    )
            )
            return@flow
        }

        val orderCache : MutableList<Order> = orderCacheDataSource.getOrders().toMutableList()

        if(orderCache.isEmpty()){
            logD(TAG, "cache is empty")
            // we need to set a interval. we can ask server each..20min or something
        }

        val orderNetwork = orderNetworkDataSource.getActiveOrders(userId)

        // Sync orders
        for(order in orderNetwork){
            val search = orderCacheDataSource.getOrder(order.id)

            if(search != null){
                orderCache.remove(order)
                orderCacheDataSource.updateOrder(order)
            }
            else{
                orderCacheDataSource.insertOrder(order)
            }
            orders.add(order)
        }

        for(oldOrder in orderCache){
            orderCacheDataSource.deleteOrder(oldOrder.id)
        }

        logD(TAG, "sync: ${orderCache.size} -- ${orderNetwork.size}")

        logD(TAG, "emit result $orderCache")
         emit(
             DataState.data(
                 response = Response(
                         message = "RetrieveOrders",
                         uiComponentType = UIComponentType.None(),
                         messageType = MessageType.None()
                 ),
                 data =  OrderListViewState(
                         orderList = orders
                 ),
                 stateEvent = stateEvent
             )
         )
    }
}