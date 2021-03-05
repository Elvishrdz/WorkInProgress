package com.eahm.learn.business.data.cache

import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.data.cache.CacheErrors.CACHE_DATA_NULL

object CacheErrors {
    const val CACHE_ERROR_UNKNOWN = "Unknown cache error"
    const val CACHE_ERROR = "cache error"
    const val CACHE_ERROR_TIMEOUT = "cache timeout"
    const val CACHE_DATA_NULL = "cache data null"
}

object CacheConstants{
    const val CACHE_TIMEOUT = 2000L
}

abstract class CacheResponseHandler <ViewState, Data>(
    private val response: CacheResult<Data?>,
    private val stateEvent: StateEvent?
){
    suspend fun getResult(): DataState<ViewState>? {

        return when(response){

            is CacheResult.GenericError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${response.errorMessage}",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }

            is CacheResult.Success -> {
                if(response.value == null){
                    DataState.error(
                        response = Response(
                            message = "${stateEvent?.errorInfo()}\n\nReason: ${CACHE_DATA_NULL}.",
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()
                        ),
                        stateEvent = stateEvent
                    )
                }
                else{
                    handleSuccess(resultObj = response.value)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>?

}