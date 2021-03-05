package com.eahm.learn.business.interactors.common.provider

import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.ProviderCacheDataSource
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.state.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertProvider<ViewState>(
    private val providerCacheDataSource: ProviderCacheDataSource
) {

    fun insertProvider(
        newProvider: Provider,
        stateEvent : StateEvent
    ) : Flow<DataState<ViewState>?> = flow{

        val cacheResponse = safeCacheCall(IO){
            providerCacheDataSource.insertProvider(newProvider)
        }

        val cacheResult = object : CacheResponseHandler<ViewState, Long>(
            response = cacheResponse,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<ViewState>? {
                return if(resultObj > 0){
                    DataState.data(
                        response = Response(
                            message = INSERT_PROVIDER_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        data = null, // todo prepare depending on the viewstate
                        stateEvent = stateEvent
                    )
                }
                else DataState.data(
                    response = Response(
                        message = INSERT_PROVIDER_FAIL,
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

    companion object{
        const val INSERT_PROVIDER_SUCCESS = "Provider successfully saved!"
        const val INSERT_PROVIDER_FAIL = "Something went wrong! Provider was NOT saved!"
    }

}