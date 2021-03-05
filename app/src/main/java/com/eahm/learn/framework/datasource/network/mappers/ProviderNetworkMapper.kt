package com.eahm.learn.framework.datasource.network.mappers

import com.eahm.learn.business.domain.factory.BusinessFactory
import com.eahm.learn.business.domain.factory.UserFactory
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.network.model.ProviderNetworkEntity
import javax.inject.Inject

class ProviderNetworkMapper
@Inject
constructor(
    private val businessFactory: BusinessFactory,
    private val userFactory: UserFactory
): EntityMapper<ProviderNetworkEntity, Provider> {

    override fun mapFromEntity(entityModel: ProviderNetworkEntity): Provider {
        return Provider(
            id = entityModel.id,
            business = businessFactory.createBusiness(entityModel.businessId),
            user = userFactory.createUser(id = entityModel.ownerUserId)
        )
    }

    override fun mapToEntity(domainModel: Provider): ProviderNetworkEntity {
        val businessId = domainModel.business?.id ?: ""
        val ownerUserId = domainModel.user?.id ?: ""

        return ProviderNetworkEntity(
            id = domainModel.id,
            businessId = businessId,
            ownerUserId = ownerUserId
        )
    }

}