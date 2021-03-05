package com.eahm.learn.business.data.network.implementation

import com.eahm.learn.business.data.network.abstraction.ProviderNetworkDataSource
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.framework.datasource.network.abstraction.ProviderFirestoreService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProviderNetworkDataSourceImpl
@Inject constructor(
    private val providerFirestoreService : ProviderFirestoreService
): ProviderNetworkDataSource {

    override suspend fun insertProvider(newProviderValues: Provider): Provider? {
        return providerFirestoreService.insertProvider(newProviderValues)
    }

    override suspend fun getProvider(providerId: String): Provider? {
        return providerFirestoreService.getProvider(providerId)
    }
}