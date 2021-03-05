package com.eahm.learn.framework.datasource.network.mappers

import com.eahm.learn.business.domain.model.Business
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.network.model.BusinessNetworkEntity

class BusinessNetworkMapper : EntityMapper<BusinessNetworkEntity, Business> {

    override fun mapFromEntity(entityModel: BusinessNetworkEntity): Business {
        return Business(
            id = entityModel.id,
            name = entityModel.name,
            status = entityModel.status
        )
    }

    override fun mapToEntity(domainModel: Business): BusinessNetworkEntity {
        return BusinessNetworkEntity(
            id = domainModel.id,
            name = domainModel.name,
            status = domainModel.status
        )
    }
}