package com.eahm.learn.business.data.cache.implementation

import com.eahm.learn.business.data.cache.abstraction.ProviderCacheDataSource
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.framework.datasource.cache.abstraction.ProviderDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProviderCacheDataSourceImpl
@Inject constructor(
    private val providerDaoService : ProviderDaoService
) : ProviderCacheDataSource {

    override suspend fun insertProvider(newProvider: Provider): Long {
        return providerDaoService.insertProvider(newProvider)
    }

    override suspend fun deleteProvider(providerId: String): Int {
        return providerDaoService.deleteProvider(providerId)
    }

    override suspend fun updateProvider(newProviderValues: Provider): Int {
        return providerDaoService.updateProvider(
            newProviderValues.id,
            newProviderValues.business?.id ?: "",
            newProviderValues.user?.id ?: "")
    }

    override suspend fun getProvider(providerId: String): Provider? {
        return providerDaoService.getProvider(providerId)
    }
}