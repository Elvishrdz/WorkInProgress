package com.eahm.learn.business.interactors.orderlistprovider

import com.eahm.learn.business.data.cache.abstraction.provider.OrderProviderCacheDataSource
import com.eahm.learn.business.data.network.abstraction.provider.OrderProviderNetworkDataSource
import com.eahm.learn.business.domain.state.DataState
import com.eahm.learn.business.domain.state.StateEvent
import com.eahm.learn.framework.presentation.providerorderlist.state.ProviderOrderListViewState
import com.eahm.learn.utils.logD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RetrieveOrdersAsProvider(
    private val orderProviderCacheDataSource: OrderProviderCacheDataSource,
    private val orderProviderNetworkDataSource: OrderProviderNetworkDataSource
) {
    private val TAG = "RetrieveOrders"

    fun retrieveOrders(
        providerId : String,
        stateEvent: StateEvent
    ) : Flow<DataState<ProviderOrderListViewState>> = flow{

        // TODO WORK ON THIS TO MAKE IT WORK PROPERLY
        logD(TAG, "obtain orders in network")
        val allServerOrders = orderProviderNetworkDataSource.getOrders(providerId)

        logD(TAG, "obtain orders in cache")
        val allCachedOrders = orderProviderCacheDataSource.getOrders()

        logD(TAG, "synchronize data. Network: ${allServerOrders.size} - Cache: ${allCachedOrders.size}")
        for(latestOrder in allServerOrders){
            orderProviderCacheDataSource.getOrder(latestOrder.id)?.let { existingOrder ->
                logD(TAG, "Update order with new server values")
                orderProviderCacheDataSource.updateOrder(latestOrder)
                allCachedOrders.dropWhile {
                    it.id == existingOrder.id
                }
            } ?: orderProviderCacheDataSource.insertOrder(latestOrder)
        }

        logD(TAG, "Remove deprecated orders")
        for(deprecatedOrder in allCachedOrders){
            orderProviderCacheDataSource.getOrder(deprecatedOrder.id)?.let {
                orderProviderCacheDataSource.deleteOrder(it.id)
            }
        }


        logD(TAG, "all synchronized! total: ${allServerOrders.size}")
        emit(
            DataState.data(
                null,
                data = ProviderOrderListViewState(
                    orderProviderList = allServerOrders
                ),
                stateEvent = stateEvent
            )
        )

    }
}