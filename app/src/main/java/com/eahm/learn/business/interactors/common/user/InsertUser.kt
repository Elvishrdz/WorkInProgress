package com.eahm.learn.business.interactors.common.user

import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.UserCacheDataSource
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.business.domain.state.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertUser <ViewState>(
    private val userCacheDataSource: UserCacheDataSource
) {

    fun insertUser(
            newUser: User,
            stateEvent : StateEvent
    ) : Flow<DataState<ViewState>?> = flow{

        val cacheResponse = safeCacheCall(Dispatchers.IO){
            userCacheDataSource.insertUser(newUser)
        }

        val cacheResult = object : CacheResponseHandler<ViewState, Long>(
            response = cacheResponse,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<ViewState>? {
                return if(resultObj > 0){
                    DataState.data(
                        response = Response(
                            message = INSERT_USER_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        data = null, // todo prepare depending on the viewstate
                        stateEvent = stateEvent
                    )
                }
                else DataState.data(
                    response = Response(
                        message = INSERT_USER_FAIL,
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
        const val INSERT_USER_SUCCESS = "User successfully saved!"
        const val INSERT_USER_FAIL = "Something went wrong! User was NOT saved!"
    }

}