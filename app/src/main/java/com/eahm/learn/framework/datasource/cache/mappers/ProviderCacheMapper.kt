package com.eahm.learn.framework.datasource.cache.mappers

import com.eahm.learn.business.domain.factory.BusinessFactory
import com.eahm.learn.business.domain.factory.UserFactory
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.cache.model.ProviderCacheEntity

class ProviderCacheMapper(
    private val businessFactory : BusinessFactory,
    private val userFactory : UserFactory
) : EntityMapper<ProviderCacheEntity, Provider> {

    override fun mapFromEntity(entityModel: ProviderCacheEntity): Provider {
        return Provider(
            id = entityModel.id,
            business = businessFactory.createBusiness(id = entityModel.businessId),
            user = userFactory.createUser(id = entityModel.ownerUserId),
        )
    }

    override fun mapToEntity(domainModel: Provider): ProviderCacheEntity {
        val businessId = domainModel.business?.id ?: ""
        val ownerUserId = domainModel.user?.id ?: ""

        return ProviderCacheEntity(
            id = domainModel.id,
            businessId = businessId,
            ownerUserId = ownerUserId
        )
    }


}