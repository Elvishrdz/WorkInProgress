package com.eahm.learn.business.data.network.abstraction

import com.eahm.learn.business.domain.model.Provider

interface ProviderNetworkDataSource {
    suspend fun insertProvider(newProviderValues : Provider) : Provider?
    suspend fun getProvider(providerId : String) : Provider?
    //    suspend fun deleteProvider(providerId: String)
}
