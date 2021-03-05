package com.eahm.learn.business.data.cache.abstraction

import com.eahm.learn.business.domain.model.Provider

interface ProviderCacheDataSource {

    suspend fun insertProvider(newProvider : Provider) : Long
    suspend fun deleteProvider(providerId: String) : Int
    suspend fun updateProvider(newProviderValues : Provider) : Int
    suspend fun getProvider(providerId : String) : Provider?

}