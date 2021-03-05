package com.eahm.learn.framework.datasource.network.mappers

import com.eahm.learn.business.domain.factory.UserFactory
import com.eahm.learn.business.domain.model.Address
import com.eahm.learn.business.domain.model.Location
import com.eahm.learn.business.domain.model.Phone
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.business.domain.util.DateUtil
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.network.model.UserAddressNetworkEntity
import com.eahm.learn.framework.datasource.network.model.UserNetworkEntity
import com.eahm.learn.framework.datasource.network.model.UserPhoneNetworkEntity
import com.google.firebase.firestore.GeoPoint
import javax.inject.Inject

class UserNetworkMapper
@Inject constructor(
    private val dateUtil: DateUtil,
    private val userAddressMapper : UserAddressNetworkMapper,
    private val userPhoneNumberMapper : UserPhoneNumberNetworkMapper,
    private val userFactory : UserFactory
): EntityMapper<UserNetworkEntity, User> {

    override fun mapFromEntity(entityModel: UserNetworkEntity): User {
        val address = userAddressMapper.mapListFromEntity(entityModel.addresses)
        val phones  = userPhoneNumberMapper.mapListFromEntity(entityModel.phoneNumber)

        return userFactory.createUser(
            entityModel,
            address,
            phones
        )
    }

    override fun mapToEntity(domainModel: User): UserNetworkEntity {
        return UserNetworkEntity(
            id = domainModel.id,
            name_first = domainModel.name_first,
            name_second  = domainModel.name_second,
            last_name_first= domainModel.last_name_first,
            last_name_second= domainModel.last_name_second,
            addresses = userAddressMapper.mapListToEntity(domainModel.addresses),
            phoneNumber  = userPhoneNumberMapper.mapListToEntity(domainModel.phoneNumber),
            providerId = domainModel.providerId,
            date_birth  = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.dateBirth),
            created_at  = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.created_at),
            status  = domainModel.status
        )
    }
}

class UserAddressNetworkMapper : EntityMapper<UserAddressNetworkEntity, Address> {
    override fun mapFromEntity(entityModel: UserAddressNetworkEntity): Address {
        return Address(
            street = entityModel.street,
            buildingNumber = entityModel.buildingNumber,
            postCode = entityModel.postCode,
            countryCode = entityModel.countryCode,
            description = entityModel.description,
            geolocation = entityModel.geolocation.toLocation()
        )
    }

    override fun mapToEntity(domainModel: Address): UserAddressNetworkEntity {
        return UserAddressNetworkEntity(
            street = domainModel.street,
            buildingNumber = domainModel.buildingNumber,
            postCode = domainModel.postCode,
            countryCode = domainModel.countryCode,
            description = domainModel.description,
            geolocation = domainModel.geolocation.toGeoPoint()
        )
    }

    fun mapListFromEntity(list : List<UserAddressNetworkEntity>) : List<Address>{
        return list.map { mapFromEntity(it) }
    }

    fun mapListToEntity(list: List<Address>): List<UserAddressNetworkEntity> {
        return list.map { mapToEntity(it) }
    }

}

class UserPhoneNumberNetworkMapper : EntityMapper<UserPhoneNetworkEntity, Phone>{
    override fun mapFromEntity(entityModel: UserPhoneNetworkEntity): Phone {
        return Phone(
            extension = entityModel.extension,
            number = entityModel.number,
            description = entityModel.description
        )
    }

    override fun mapToEntity(domainModel: Phone): UserPhoneNetworkEntity {
        return UserPhoneNetworkEntity(
            extension = domainModel.extension,
            number = domainModel.number,
            description = domainModel.description
        )
    }

    fun mapListFromEntity(list : List<UserPhoneNetworkEntity>) : List<Phone>{
        return list.map { mapFromEntity(it) }
    }

    fun mapListToEntity(list: List<Phone>): List<UserPhoneNetworkEntity> {
        return list.map { mapToEntity(it) }
    }

}

// LOCATION EXTENSIONS
fun GeoPoint.toLocation() : Location {
    return Location(
        latitud = this.latitude,
        longitud = this.longitude
    )
}

fun Location.toGeoPoint() : GeoPoint{
    return GeoPoint(
        this.latitud,
        this.longitud
    )
}
