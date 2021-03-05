package com.eahm.learn.business.data.cache.implementation

import com.eahm.learn.business.data.cache.abstraction.UserCacheDataSource
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.framework.datasource.cache.abstraction.UserDaoService

class UserCacheDataSourceImpl(
    private val userDaoService : UserDaoService
) : UserCacheDataSource {

    override suspend fun insertUser(newUser: User): Long {
        return userDaoService.insertUser(newUser)
    }

    override suspend fun deleteUser(userId: String): Int {
        return userDaoService.deleteUser(userId)
    }

    override suspend fun deleteUsers(): Int {
        return userDaoService.deleteUsers()
    }

    override suspend fun getUser(userId: String): User? {
        return userDaoService.getUser(userId)
    }

    override suspend fun insertClient(newUser: User): Long {
        return userDaoService.insertClient(newUser)
    }

    override suspend fun deleteClient(): Int {
        return userDaoService.deleteClient()
    }

    override suspend fun getClient(userId: String): User? {
        return userDaoService.getClient(userId)
    }
}