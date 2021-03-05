package com.eahm.learn.framework.datasource.cache.abstraction

import com.eahm.learn.business.domain.model.Provider

interface ProviderDaoService {
    suspend fun insertProvider(newProvider : Provider) : Long
    suspend fun deleteProvider(providerId: String) : Int
    suspend fun updateProvider(providerId: String, businessId : String, ownerUserId : String) : Int
    suspend fun getProvider(providerId : String) : Provider?
}