package com.eahm.learn.business.domain.factory

import com.eahm.learn.business.domain.model.Address
import com.eahm.learn.business.domain.model.Phone
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.business.domain.util.DateUtil
import com.eahm.learn.framework.datasource.cache.model.ClientCacheEntity
import com.eahm.learn.framework.datasource.cache.model.UserCacheEntity
import com.eahm.learn.framework.datasource.network.model.UserNetworkEntity
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserFactory
@Inject constructor(
    private val dateUtil : DateUtil
){

    fun createUser(
        id: String,
        name : String = "",
        secondName : String  = "",
        firstLastName : String  = "",
        secondLastName : String  = "",
        email : String = "",
        addresses : List<Address> = listOf(), //List<Address>
        phoneNumber : List<Phone> = listOf(), //List<Phone>
        dateBirth : String  = "",
        providerId : String  = ""
    ): User {
        return User(
            id = if(id.isNotEmpty()) id else UUID.randomUUID().toString(),
            name_first = name,
            name_second = secondName,
            last_name_first = firstLastName,
            last_name_second = secondLastName,
            email = email,
            addresses = addresses, //List<Address>
            phoneNumber = phoneNumber, //List<Phone>
            dateBirth = dateBirth,
            providerId = providerId,
            created_at = dateUtil.getCurrentTimestamp(),
            status = "" // todo set default status
        )
    }

    fun createUser(
        user : User,
        name : String = "",
        email : String = ""
    ): User? {
        return if(user.id.isNotEmpty()) {
            User(
                id = user.id,
                name_first = if(name.isEmpty()) user.name_first else name,
                name_second = user.name_second,
                last_name_first = user.last_name_first,
                last_name_second = user.last_name_second,
                email = email,
                addresses = user.addresses,
                phoneNumber = user.phoneNumber,
                dateBirth = user.dateBirth,
                providerId = user.providerId,
                created_at = user.created_at,
                status = user.status
            )
        } else null
    }

    fun createUser(
            entityModel : UserCacheEntity,
            addresses: List<Address>,
            phones: List<Phone>
    ) : User {
        return User(
            id = entityModel.id,
            name_first = entityModel.name_first,
            name_second = entityModel.name_second,
            last_name_first = entityModel.last_name_first,
            last_name_second = entityModel.last_name_second,
            email = "",
            addresses = addresses,
            phoneNumber = phones,
            dateBirth = entityModel.dateBirth,
            providerId = entityModel.providerId,
            created_at = entityModel.created_at,
            status = entityModel.status
        )
    }

    fun createUser(
        entityModel: UserNetworkEntity,
        addresses: List<Address>,
        phones: List<Phone>
    ): User {
        return User(
            id = entityModel.id,
            name_first = entityModel.name_first,
            name_second  = entityModel.name_second,
            last_name_first= entityModel.last_name_first,
            last_name_second= entityModel.last_name_second,
            email = "",
            addresses = addresses,
            phoneNumber = phones,
            providerId = entityModel.providerId,
            dateBirth  = dateUtil.convertFirebaseTimestampToStringData(entityModel.date_birth),
            created_at  = dateUtil.convertFirebaseTimestampToStringData(entityModel.created_at),
            status  = entityModel.status
        )
    }

    fun createUser(entityModel: ClientCacheEntity, addresses: List<Address>, phones: List<Phone>): User {
        return User(
            id = entityModel.id,
            name_first = entityModel.name_first,
            name_second = entityModel.name_second,
            last_name_first = entityModel.last_name_first,
            last_name_second = entityModel.last_name_second,
            email = "",
            addresses = addresses,
            phoneNumber = phones,
            dateBirth = entityModel.dateBirth,
            providerId = entityModel.providerId,
            created_at = entityModel.created_at,
            status = entityModel.status
        )
    }

    fun createEmptyUser(): User {
        return User(
            id = "",
            name_first = "",
            name_second = "",
            last_name_first = "",
            last_name_second = "",
            email = "",
            addresses = listOf(),
            phoneNumber = listOf(),
            dateBirth = "",
            providerId = "",
            created_at = "",
            status = "inactive"
        )
    }
}