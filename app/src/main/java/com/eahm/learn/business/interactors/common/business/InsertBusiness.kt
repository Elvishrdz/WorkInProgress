package com.eahm.learn.business.interactors.common.business

import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.BusinessCacheDataSource
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.business.domain.model.Business
import com.eahm.learn.business.domain.state.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertBusiness <ViewState>(
    private val businessDataSource : BusinessCacheDataSource
) {

    fun insertBusiness(
            newBusiness : Business,
            stateEvent : StateEvent
    ) : Flow<DataState<ViewState>?> = flow{

        val cacheResponse = safeCacheCall(IO){
            businessDataSource.insertBusiness(newBusiness)
        }

        val cacheResult = object : CacheResponseHandler<ViewState, Long>(
            response = cacheResponse,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<ViewState>? {
                return if(resultObj > 0){
                    DataState.data(
                        response = Response(
                            message = INSERT_BUSINESS_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        data = null, // todo prepare depending on the viewstate
                        stateEvent = stateEvent
                    )
                }
                else DataState.data(
                    response = Response(
                        message = INSERT_BUSINESS_SUCCESS,
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
        const val INSERT_BUSINESS_SUCCESS = "Business successfully saved!"
        const val INSERT_BUSINESS_FAIL = "Something went wrong! Business was NOT saved!"
    }
}