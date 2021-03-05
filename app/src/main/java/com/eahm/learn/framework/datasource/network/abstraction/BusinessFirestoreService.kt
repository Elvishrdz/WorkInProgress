package com.eahm.learn.framework.datasource.network.abstraction

import com.eahm.learn.business.domain.model.Business

interface BusinessFirestoreService {
    /* suspend fun insertBusiness(newBusiness: Business) : Long
     suspend fun updateBusiness(newBusinessValues : Business) : Int
     suspend fun deleteBusiness(businessId: String) : Int*/
    suspend fun getBusiness(businessId : String) : Business?
}