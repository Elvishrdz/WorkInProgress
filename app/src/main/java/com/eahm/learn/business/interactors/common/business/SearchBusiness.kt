package com.eahm.learn.business.interactors.common.business

import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.BusinessCacheDataSource
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.business.domain.model.Business
import com.eahm.learn.business.domain.state.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchBusiness  <ViewState>  (
    private val businessCacheDataSource: BusinessCacheDataSource
) {

    fun searchBusiness(
        businessId : String,
        stateEvent : StateEvent
    ) : Flow<DataState<ViewState>?> = flow{

        val cacheResponse = safeCacheCall(Dispatchers.IO){
            businessCacheDataSource.getBusiness(businessId)
        }

        val cacheResult = object : CacheResponseHandler<ViewState, Business?>(
            response = cacheResponse,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Business?): DataState<ViewState>? {
                return if(resultObj != null){
                    DataState.data(
                        response = Response(
                            message = SEARCH_BUSINESS_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        data = null, // todo prepare depending on the viewstate
                        stateEvent = stateEvent
                    )
                }
                else DataState.data(
                    response = Response(
                        message = SEARCH_BUSINESS_FAIL,
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
        const val SEARCH_BUSINESS_SUCCESS = "Business successfully founded!"
        const val SEARCH_BUSINESS_FAIL = "Business not founded!"
    }
}