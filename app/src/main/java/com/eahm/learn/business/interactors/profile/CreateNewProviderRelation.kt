package com.eahm.learn.business.interactors.profile

import com.eahm.learn.business.data.cache.abstraction.ProviderCacheDataSource
import com.eahm.learn.business.data.cache.abstraction.UserCacheDataSource
import com.eahm.learn.business.data.network.abstraction.ProviderNetworkDataSource
import com.eahm.learn.business.data.network.abstraction.UserNetworkDataSource
import com.eahm.learn.business.data.utils.safeApiCall
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.framework.presentation.profile.state.ProfileViewState
import com.eahm.learn.utils.logD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CreateNewProviderRelation(
    private val providerCacheDataSource: ProviderCacheDataSource,
    private val providerNetworkDataSource: ProviderNetworkDataSource,
    private val userNetworkDataSource: UserNetworkDataSource,
    private val userCacheDataSource: UserCacheDataSource
) {

    private val TAG = "CreateNewProviderRelation"

    fun setUserToUserProvider(
            provider: Provider,
            stateEvent: StateEvent
    )  : Flow<DataState<ProfileViewState>?> = flow {

        val data : ProfileViewState? = null
        var emitResult = DataState.data(
                response = Response(
                        message = "ERROR",
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Error()
                ),
                data = data,
                stateEvent = stateEvent
        )

        logD(TAG, "obtain current user")
        val userId = provider.user?.id

        if(userId != null && userId.isNotEmpty()){
            logD(TAG, "Verify that user exists and have data")
            val userExists = userNetworkDataSource.getUser(userId)

            userExists?.let {
                if(userExists.providerId.isEmpty()){
                    logD(TAG, "create new provider")
                    val results = providerNetworkDataSource.insertProvider(provider)

                    results?.let {
                        logD(TAG, "update user ${userExists.id} in provider ${results.id}")
                        val updatingProvider = userNetworkDataSource.setProviderId(userExists.id, results.id)

                        if(updatingProvider){
                            logD(TAG, "save new provider in cache")
                            providerCacheDataSource.insertProvider(provider)

                            //todo improvde this
                            logD(TAG, "update user information in cache")
                            userCacheDataSource.deleteClient()
                            val updatedUser = userNetworkDataSource.getUser(userExists.id)

                            updatedUser?.let {
                                logD(TAG, "current user updated!")
                                userCacheDataSource.insertClient(it)

                                emitResult = DataState.data(
                                        response = Response(
                                                message = "SUCCESS",
                                                uiComponentType = UIComponentType.None(),
                                                messageType = MessageType.Error()
                                        ),
                                        data = ProfileViewState(updatedUser),
                                        stateEvent = stateEvent
                                )
                            }
                        }
                    }
                }
                else {
                    logD(TAG, "the user contains an existing provider id")
                }
            }
        }

        emit(emitResult)

    }
}