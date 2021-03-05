package com.eahm.learn.framework.datasource.cache.mappers

import com.eahm.learn.business.domain.model.Business
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.cache.model.BusinessCacheEntity

class BusinessCacheMapper : EntityMapper<Business, BusinessCacheEntity> {

    override fun mapFromEntity(entityModel: Business): BusinessCacheEntity {
        return BusinessCacheEntity(
            id = entityModel.id,
            name = entityModel.name,
            status = entityModel.status
        )
    }

    override fun mapToEntity(domainModel: BusinessCacheEntity): Business {
        return Business(
            id = domainModel.id,
            name = domainModel.name,
            status = domainModel.status
        )
    }
}