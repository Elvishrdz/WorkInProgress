package com.eahm.learn.framework.datasource.cache.implementation

import com.eahm.learn.business.domain.model.Business
import com.eahm.learn.framework.datasource.cache.abstraction.BusinessDaoService
import com.eahm.learn.framework.datasource.cache.dao.BusinessDao
import com.eahm.learn.framework.datasource.cache.mappers.BusinessCacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BusinessDaoServiceImpl
@Inject constructor(
    private val businessDao : BusinessDao,
    private val cacheMapper : BusinessCacheMapper
) : BusinessDaoService {

    override suspend fun insertBusiness(newBusiness: Business): Long {
        return businessDao.insertBusiness(
            cacheMapper.mapFromEntity(newBusiness)
        )
    }

    override suspend fun updateBusiness(newBusinessValues: Business): Int {
        val entity = cacheMapper.mapFromEntity(newBusinessValues)

        return businessDao.updateBusiness(
            entity.id,
            entity.name,
            entity.status
        )
    }

    override suspend fun deleteBusiness(businessId: String): Int {
        return businessDao.deleteBusiness(businessId)
    }

    override suspend fun getBusiness(businessId: String): Business? {
        return businessDao.getBusiness(businessId)?.let {
            cacheMapper.mapToEntity(
                it
            )
        }
    }
}