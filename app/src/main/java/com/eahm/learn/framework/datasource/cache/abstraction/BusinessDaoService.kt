package com.eahm.learn.framework.datasource.cache.abstraction

import com.eahm.learn.business.domain.model.Business

interface BusinessDaoService {

    suspend fun insertBusiness(newBusiness: Business) : Long
    suspend fun updateBusiness(newBusinessValues : Business) : Int
    suspend fun deleteBusiness(businessId: String) : Int
    suspend fun getBusiness(businessId : String) : Business?

}