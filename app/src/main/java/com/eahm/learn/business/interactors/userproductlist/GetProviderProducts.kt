package com.eahm.learn.business.interactors.userproductlist

import com.eahm.learn.business.data.cache.abstraction.provider.ProductProviderCacheDataSource
import com.eahm.learn.business.data.network.abstraction.provider.ProductProviderNetworkDataSource
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.framework.presentation.providerproductlist.state.UserProductListViewState
import com.eahm.learn.utils.logD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetProviderProducts(
    private val productProviderCacheDataSource: ProductProviderCacheDataSource,
    private val productProviderNetworkDataSource: ProductProviderNetworkDataSource
) {

    private val TAG = "GetProviderProducts"

    fun getProviderProducts(
            providerId : String,
            stateEvent: StateEvent
    ) : Flow<DataState<UserProductListViewState>> = flow{

        //check if empty to emit an error
        logD(TAG, "get providers from $providerId")

        val currentProducts = productProviderNetworkDataSource.getAllProductsProviderIds(providerId)
        //val cachedProducts = productProviderCacheDataSource.getAllProductsProviderIds()

        val data = UserProductListViewState(
                userProductList = currentProducts
        )

        emit(
             DataState.data(
                     response = Response(
                             message = null,
                             uiComponentType = UIComponentType.None(),
                             messageType = MessageType.Success()
                     ),
                     data = data,
                     stateEvent = stateEvent
             )
        )


    }
}