package com.eahm.learn.business.data.network.implementation

import com.eahm.learn.business.data.network.abstraction.BusinessNetworkDataSource
import com.eahm.learn.business.domain.model.Business
import com.eahm.learn.framework.datasource.network.abstraction.BusinessFirestoreService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BusinessNetworkDataSourceImpl
@Inject constructor(
    private val firestoreService : BusinessFirestoreService
) : BusinessNetworkDataSource {

    override suspend fun getBusiness(businessId: String): Business? {
        return firestoreService.getBusiness(businessId)
    }
}