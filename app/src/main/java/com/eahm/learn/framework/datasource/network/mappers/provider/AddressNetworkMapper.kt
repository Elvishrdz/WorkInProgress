package com.eahm.learn.framework.datasource.network.mappers.provider

import com.eahm.learn.business.domain.model.Address
import com.eahm.learn.business.domain.model.Location
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.network.model.provider.AddressNetworkEntity

class AddressNetworkMapper : EntityMapper<AddressNetworkEntity, Address> {
    override fun mapFromEntity(entityModel: AddressNetworkEntity): Address {
        return Address(
                street = "",
                buildingNumber = "",
                postCode = entityModel.postCode,
                countryCode = entityModel.country,
                description = entityModel.description,
                geolocation = Location()
        )
    }

    override fun mapToEntity(domainModel: Address): AddressNetworkEntity {
        return AddressNetworkEntity(
                address = "",
                city = "",
                country = domainModel.countryCode,
                description = domainModel.description,
                postCode = domainModel.postCode,
                town = ""
        )
    }


}