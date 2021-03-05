package com.eahm.learn.framework.datasource.network.abstraction

import com.eahm.learn.business.domain.model.Provider

interface ProviderFirestoreService {
    suspend fun insertProvider(newProviderValues : Provider) : Provider?
    suspend fun getProvider(providerId : String) : Provider?
    //  suspend fun deleteProvider(providerId: String)
}