package com.eahm.learn.business.data.cache.implementation

import com.eahm.learn.business.data.cache.abstraction.BusinessCacheDataSource
import com.eahm.learn.business.domain.model.Business
import com.eahm.learn.framework.datasource.cache.abstraction.BusinessDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BusinessCacheDataSourceImpl
@Inject constructor(
    private val businessDaoService: BusinessDaoService
) : BusinessCacheDataSource {

    override suspend fun insertBusiness(newBusiness: Business): Long {
        return businessDaoService.insertBusiness(newBusiness)
    }

    override suspend fun updateBusiness(newBusinessValues: Business): Int {
        return businessDaoService.updateBusiness(newBusinessValues)
    }

    override suspend fun deleteBusiness(businessId: String): Int {
        return businessDaoService.deleteBusiness(businessId)
    }

    override suspend fun getBusiness(businessId: String): Business? {
        return businessDaoService.getBusiness(businessId)
    }
}