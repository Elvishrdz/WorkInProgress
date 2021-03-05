package com.eahm.learn.framework.datasource.cache.mappers

import android.annotation.SuppressLint
import android.util.Log
import com.eahm.learn.business.domain.factory.UserFactory
import com.eahm.learn.business.domain.model.Address
import com.eahm.learn.business.domain.model.Phone
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.business.domain.util.EntityMapper
import com.eahm.learn.framework.datasource.cache.model.ClientCacheEntity
import com.eahm.learn.framework.datasource.cache.model.UserCacheEntity
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

@Suppress("UnstableApiUsage", "SpellCheckingInspection", "PrivatePropertyName")
@SuppressLint("LogNotTimber")
class UserCacheMapper(
    private val gson : Gson,
    private val userFactory: UserFactory
) : EntityMapper<UserCacheEntity, User> {

    private val TAG = "UserCacheMapper"

    override fun mapFromEntity(entityModel: UserCacheEntity): User {
        val addressListType: Type = object : TypeToken<ArrayList<Address?>?>() {}.type
        val addresses: List<Address> = gson.fromJson(entityModel.addresses, addressListType)
        
        val phoneListType : Type = object : TypeToken<ArrayList<Phone?>?>() {}.type
        val phones : List<Phone> = gson.fromJson(entityModel.phoneNumber, phoneListType)
        Log.d(TAG, "Convert to Models: ${addresses.size} || ${phones.size}")

        return userFactory.createUser(
            entityModel,
            addresses,
            phones
        )
    }

    override fun mapToEntity(domainModel: User): UserCacheEntity {
        val addresses = gson.toJson(domainModel.addresses)
        val phones = gson.toJson(domainModel.phoneNumber)
        Log.d(TAG, "convertToJSON: $addresses || $phones")

        return UserCacheEntity(
            id = domainModel.id,
            name_first = domainModel.name_first,
            name_second = domainModel.name_second,
            last_name_first = domainModel.last_name_first,
            last_name_second = domainModel.last_name_second,
            addresses = addresses,
            phoneNumber = phones,
            providerId = domainModel.providerId,
            dateBirth = domainModel.dateBirth,
            created_at = domainModel.created_at,
            status = domainModel.status
        )
    }
}


@Suppress("UnstableApiUsage", "SpellCheckingInspection", "PrivatePropertyName")
@SuppressLint("LogNotTimber")
class ClientCacheMapper(
    private val gson : Gson,
    private val userFactory: UserFactory
) : EntityMapper<ClientCacheEntity, User> {

    private val TAG = "UserCacheMapper"

    override fun mapFromEntity(entityModel: ClientCacheEntity): User {
        val addressListType: Type = object : TypeToken<ArrayList<Address?>?>() {}.type
        val addresses: List<Address> = gson.fromJson(entityModel.addresses, addressListType)

        val phoneListType : Type = object : TypeToken<ArrayList<Phone?>?>() {}.type
        val phones : List<Phone> = gson.fromJson(entityModel.phoneNumber, phoneListType)
        Log.d(TAG, "Convert to Models: ${addresses.size} || ${phones.size}")

        return userFactory.createUser(
            entityModel,
            addresses,
            phones
        )
    }

    override fun mapToEntity(domainModel: User): ClientCacheEntity {
        val addresses = gson.toJson(domainModel.addresses)
        val phones = gson.toJson(domainModel.phoneNumber)
        Log.d(TAG, "convertToJSON: $addresses || $phones")

        return ClientCacheEntity(
            id = domainModel.id,
            name_first = domainModel.name_first,
            name_second = domainModel.name_second,
            last_name_first = domainModel.last_name_first,
            last_name_second = domainModel.last_name_second,
            addresses = addresses,
            phoneNumber = phones,
            providerId = domainModel.providerId,
            dateBirth = domainModel.dateBirth,
            created_at = domainModel.created_at,
            status = domainModel.status
        )
    }
}
 