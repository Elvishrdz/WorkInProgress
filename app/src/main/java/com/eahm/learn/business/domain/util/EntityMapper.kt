package com.eahm.learn.business.domain.util

interface EntityMapper<EntityModel, DomainModel> {
    fun mapFromEntity(entityModel : EntityModel) : DomainModel
    fun mapToEntity(domainModel: DomainModel) : EntityModel

}


