package com.eahm.learn.business.interactors.common.provider

import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.ProviderCacheDataSource
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.framework.presentation.productdetail.state.ProductDetailStateEvent
import com.eahm.learn.framework.presentation.productdetail.state.ProductDetailViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchProvider <ViewState> (
    private val providerCacheDataSource: ProviderCacheDataSource
) {

    fun searchProvider(
       providerId : String,
       stateEvent : StateEvent
    ) : Flow<DataState<ViewState>?> = flow{

        val cacheResult = cacheResponseHandler(providerId, stateEvent)

        emit(cacheResult.getResult())


    }

    suspend fun getCachedProvider(
        providerId : String,
        stateEvent : StateEvent
    ) : Provider? {
        val cacheResult = cacheResponseHandler(providerId, stateEvent)

        when(stateEvent){
            is ProductDetailStateEvent ->{
                val data = cacheResult.getResult()?.data as ProductDetailViewState
                return data.provider
            }
        }

        return null
    }

    private suspend fun cacheResponseHandler(
        providerId: String,
        stateEvent: StateEvent
    ) : CacheResponseHandler<ViewState, Provider?> {
        val cacheResponse = safeCacheCall(Dispatchers.IO){
            providerCacheDataSource.getProvider(providerId)
        }

        return object : CacheResponseHandler<ViewState, Provider?>(
            response = cacheResponse,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Provider?): DataState<ViewState>? {
                return if(resultObj != null){

                    when(stateEvent){
                        is ProductDetailStateEvent -> {
                            val data = ProductDetailViewState(
                                provider = resultObj
                            ) as ViewState

                            DataState.data(
                                response = Response(
                                    message = SEARCH_PROVIDER_SUCCESS,
                                    uiComponentType = UIComponentType.None(),
                                    messageType = MessageType.Success()
                                ),
                                data = data,
                                stateEvent = stateEvent
                            )
                        }
                        else ->  failResult(stateEvent)
                    }


                }
                else failResult(stateEvent)
            }
        }
    }

    private fun failResult(stateEvent : StateEvent): DataState<ViewState> {
        return DataState.data(
            response = Response(
                message = SEARCH_PROVIDER_FAIL,
                uiComponentType = UIComponentType.Toast(),
                messageType = MessageType.Error()
            ),
            data = null,
            stateEvent = stateEvent
        )
    }

    companion object{
       const val SEARCH_PROVIDER_SUCCESS = "Provider successfully founded!"
       const val SEARCH_PROVIDER_FAIL = "Provider not founded!"
    }
}