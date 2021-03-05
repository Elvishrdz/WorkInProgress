package com.eahm.learn.business.interactors.common.sync

import com.eahm.learn.business.data.cache.CacheResponseHandler
import com.eahm.learn.business.data.cache.abstraction.BusinessCacheDataSource
import com.eahm.learn.business.data.cache.abstraction.ProviderCacheDataSource
import com.eahm.learn.business.data.cache.abstraction.UserCacheDataSource
import com.eahm.learn.business.data.network.ApiResponseHandler
import com.eahm.learn.business.data.network.abstraction.BusinessNetworkDataSource
import com.eahm.learn.business.data.network.abstraction.ProviderNetworkDataSource
import com.eahm.learn.business.data.network.abstraction.UserNetworkDataSource
import com.eahm.learn.business.data.utils.safeApiCall
import com.eahm.learn.business.data.utils.safeCacheCall
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.model.Business
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.framework.presentation.productdetail.state.ProductDetailViewState
import com.eahm.learn.utils.logD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SyncProvider(
    private val providerNetworkDataSource: ProviderNetworkDataSource,
    private val businessNetworkDataSource: BusinessNetworkDataSource,
    private val userNetworkDataSource: UserNetworkDataSource,

    private val providerCacheDataSource: ProviderCacheDataSource,
    private val businessCacheDataSource: BusinessCacheDataSource,
    private val userCacheDataSource: UserCacheDataSource,
) {

    private val TAG = "SyncProvider"

    fun syncProvider(
        providerId : String,
        stateEvent : StateEvent? = null
    ) : Flow<DataState<ProductDetailViewState>> = flow{

        // Is the provider already in cache?
        val cachedProvider = getCurrentProvider(providerId)
        logD(TAG, "current cache provider: $cachedProvider")

        if(cachedProvider != null){
            logD(TAG, "check if contains the business or user information")
            var currentBusiness : Business? = null
            var currentUser : User? = null

            // obtain business information from cache
            currentBusiness = businessCacheDataSource.getBusiness(cachedProvider.business?.id ?: "")
                              ?:
                              getBusinessFromServerAndSaveInCache(cachedProvider.business?.id)  // get business from server and save it in cache

            // obtain userOwner information from server
            currentUser = userCacheDataSource.getUser(cachedProvider.user?.id ?: "")
                          ?:
                          getUserFromServerAndSaveInCache(cachedProvider.user?.id)

            // Return the provider with all the information
            val dataResponse = ProductDetailViewState(
                provider = Provider(
                    id = cachedProvider.id,
                    business = currentBusiness,
                    user = currentUser
                )
            )

            emit(DataState.data(
                response = Response(
                    message = "PROVIDER SYNC WAS SUCCESSFUL",
                    uiComponentType = UIComponentType.None(),
                    messageType = MessageType.Success()
                ),
                data = dataResponse,
                stateEvent = stateEvent
            ))
        }
        else {
            logD(TAG, "get provider from server and save it in cache")
            val networkProviderResponse = getProviderFromServer(providerId)
            val providerSuccessfullySaved = insertProvider(networkProviderResponse)

            // get business from server and save it in cache
            val networkBusinessResponse = getBusinessFromServerAndSaveInCache(networkProviderResponse?.business?.id)

            // get user from server and save it in cache
            val networkUserResponse = getUserFromServerAndSaveInCache(networkProviderResponse?.user?.id)

            logD(TAG, "Sync results: Provider = $providerSuccessfullySaved - Business: ${networkBusinessResponse?.name} - User: ${networkUserResponse?.name_first}")

            emit(
                DataState.data(
                    response = Response("",uiComponentType = UIComponentType.None(), messageType = MessageType.Success()),
                    data = ProductDetailViewState(
                        provider = Provider(
                            providerId,
                            networkBusinessResponse,
                            networkUserResponse
                        )
                    ),
                    stateEvent = stateEvent
                )
            )
        }

    }

    private suspend fun getCurrentProvider(providerId: String): Provider? {

        val cacheResponse = safeCacheCall(IO){
            providerCacheDataSource.getProvider(providerId)
        }

        return object : CacheResponseHandler<Provider, Provider?>(
            response = cacheResponse,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: Provider?): DataState<Provider>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }

        }.getResult()?.data
    }

    private suspend fun getBusinessFromServerAndSaveInCache(businessId: String?) : Business? {
        return businessId?.let {
            val networkBusinessResponse = getBusinessFromServer(businessId)
            val businessSuccessfullySaved = insertBusiness(networkBusinessResponse)

            logD(TAG, "getBusinessFromServerAndSaveInCache id $businessId - network response: ${networkBusinessResponse?.name} - was cached: $businessSuccessfullySaved")
            networkBusinessResponse
        }
    }

    private suspend fun getUserFromServerAndSaveInCache(userId: String?) : User? {
        return userId?.let {
            val networkUserResponse = getUserFromServer(userId)
            val userSuccessfullySaved = insertUser(networkUserResponse)

            logD(TAG, "getUserFromServerAndSaveInCache id $userId - network response: ${networkUserResponse?.name_first} - was cached: $userSuccessfullySaved")
            networkUserResponse
        }
    }

    //region get data from server
    private suspend fun getProviderFromServer(
        providerId : String
    ) : Provider?{
        val networkResponse = safeApiCall(IO){
            providerNetworkDataSource.getProvider(providerId)
        }

        val networkHandler = object : ApiResponseHandler<Provider?, Provider?>(
            response = networkResponse,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: Provider?): DataState<Provider?>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }

        return networkHandler.getResult()?.data
    }

    private suspend fun getBusinessFromServer(
        businessId : String?
    ) : Business? {
        return businessId?.let{
            val networkResponse = safeApiCall(IO){
                businessNetworkDataSource.getBusiness(businessId)
            }

            val networkHandler = object : ApiResponseHandler<Business?, Business?>(
                response = networkResponse,
                stateEvent = null
            ){
                override suspend fun handleSuccess(resultObj: Business?): DataState<Business?>? {
                    return DataState.data(
                        response = null,
                        data = resultObj,
                        stateEvent = null
                    )
                }
            }

            networkHandler.getResult()?.data
        }
    }

    private suspend fun getUserFromServer(
        userId : String?
    ) : User?{
        return userId?.let {
            val networkResponse = safeApiCall(IO){
                userNetworkDataSource.getUser(userId)
            }

            val networkHandler = object : ApiResponseHandler<User?, User?>(
                response = networkResponse,
                stateEvent = null
            ){
                override suspend fun handleSuccess(resultObj: User?): DataState<User?>? {
                    return DataState.data(
                        response = null,
                        data = resultObj,
                        stateEvent = null
                    )
                }
            }

            networkHandler.getResult()?.data
        }
    }
    //endregion get data from server

    //region insert in cache
    private suspend fun insertProvider(
        newProvider: Provider?
    ) : Boolean {
        return newProvider?.let {
            val cacheResponse = safeCacheCall(IO){
                providerCacheDataSource.insertProvider(newProvider)
            }

            val cacheResult = object : CacheResponseHandler<Boolean, Long>(
                response = cacheResponse,
                stateEvent = null
            ){
                override suspend fun handleSuccess(resultObj: Long): DataState<Boolean>? {
                    return DataState.data(
                        response = null,
                        data = resultObj > 0,
                        stateEvent = null
                    )
                }
            }

            logD(TAG, "response: ${cacheResult.getResult()?.data}")
            cacheResult.getResult()?.data ?: false
        } ?: false
    }

    private suspend fun insertBusiness(
        newBusiness: Business?
    ) : Boolean {
        return newBusiness?.let {
            val cacheResponse = safeCacheCall(IO){
                businessCacheDataSource.insertBusiness(newBusiness)
            }

            val cacheResult = object : CacheResponseHandler<Boolean, Long>(
                response = cacheResponse,
                stateEvent = null
            ){
                override suspend fun handleSuccess(resultObj: Long): DataState<Boolean> {
                    return DataState.data(
                        response = null,
                        data = resultObj > 0,
                        stateEvent = null
                    )
                }
            }

            cacheResult.getResult()?.data ?: false
        } ?: false
    }

    private suspend fun insertUser(
        newUser: User?
    ) : Boolean {
        return newUser?.let {
            val cacheResponse = safeCacheCall(IO){
                userCacheDataSource.insertUser(newUser)
            }

            val cacheResult = object : CacheResponseHandler<Boolean, Long>(
                response = cacheResponse,
                stateEvent = null
            ){
                override suspend fun handleSuccess(resultObj: Long): DataState<Boolean> {
                    return DataState.data(
                        response = null,
                        data = resultObj > 0,
                        stateEvent = null
                    )
                }
            }

            cacheResult.getResult()?.data ?: false
        } ?: false
    }
    //endregion insert in cache

}