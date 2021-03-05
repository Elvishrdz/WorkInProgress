package com.eahm.learn.business.data.network

import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.data.network.NetworkErrors.NETWORK_DATA_NULL
import com.eahm.learn.business.data.network.NetworkErrors.NETWORK_ERROR

sealed class ApiResult<out T> {

    data class Success<out T> (val value : T) :ApiResult<T>()

    data class GenericError(
        val code : Int? = null,
        val errorMessage : String? = null
    ) : ApiResult<Nothing>()

    object NetworkError:ApiResult<Nothing>()

}

object NetworkConstants {
    const val NETWORK_TIMEOUT = 6000L
}

object NetworkErrors {

    const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
    const val UNABLE_TODO_OPERATION_WO_INTERNET = "Can't do that operation without an internet connection"
    const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection."
    const val NETWORK_ERROR_UNKNOWN = "Unknown network error"
    const val NETWORK_ERROR = "Network error"
    const val NETWORK_ERROR_TIMEOUT = "Network timeout"
    const val NETWORK_DATA_NULL = "Network data is null"


    fun isNetworkError(msg: String): Boolean{
        when{
            msg.contains(UNABLE_TO_RESOLVE_HOST) -> return true
            else-> return false
        }
    }
}

abstract class ApiResponseHandler <ViewState, Data>(
    private val response: ApiResult<Data?>,
    private val stateEvent: StateEvent?
){

    suspend fun getResult(): DataState<ViewState>? {
        return when(response){
            is ApiResult.GenericError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${response.errorMessage.toString()}",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.NetworkError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${NETWORK_ERROR}",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.Success -> {
                if(response.value == null){
                    DataState.error(
                        response = Response(
                            message = "${stateEvent?.errorInfo()}\n\nReason: ${NETWORK_DATA_NULL}.",
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