package com.eahm.learn.framework.datasource.cache.implementation

import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.framework.datasource.cache.abstraction.ProviderDaoService
import com.eahm.learn.framework.datasource.cache.dao.ProviderDao
import com.eahm.learn.framework.datasource.cache.mappers.ProviderCacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProviderDaoServiceImpl
@Inject constructor(
    private val providerDao : ProviderDao,
    private val providerCacheMapper : ProviderCacheMapper
) : ProviderDaoService {

    override suspend fun insertProvider(newProvider: Provider): Long {
        return providerDao.insertProvider(
            providerCacheMapper.mapToEntity(newProvider)
        )
    }

    override suspend fun deleteProvider(providerId: String): Int {
        return providerDao.deleteProvider(providerId)
    }

    override suspend fun updateProvider(
        providerId: String,
        businessId: String,
        ownerUserId: String
    ): Int {
        return providerDao.updateProvider(
            providerId,
            businessId,
            ownerUserId)
    }

    override suspend fun getProvider(providerId: String): Provider? {
        return providerDao.getProvider(providerId)?.let {
               providerCacheMapper.mapFromEntity(it)
        }
    }
}