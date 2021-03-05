package com.eahm.learn.business.data.cache.abstraction

import com.eahm.learn.business.domain.model.Business

interface BusinessCacheDataSource {

    suspend fun insertBusiness(newBusiness: Business) : Long
    suspend fun updateBusiness(newBusinessValues : Business) : Int
    suspend fun deleteBusiness(businessId: String) : Int
    suspend fun getBusiness(businessId : String) : Business?

}