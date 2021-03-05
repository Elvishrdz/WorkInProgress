package com.eahm.learn.framework.datasource.cache.implementation

import com.eahm.learn.business.domain.model.User
import com.eahm.learn.framework.datasource.cache.abstraction.UserDaoService
import com.eahm.learn.framework.datasource.cache.dao.ClientDao
import com.eahm.learn.framework.datasource.cache.dao.UserDao
import com.eahm.learn.framework.datasource.cache.mappers.ClientCacheMapper
import com.eahm.learn.framework.datasource.cache.mappers.UserCacheMapper

class UserDaoServiceImpl(
    private val userDao : UserDao,
    private val clientDao: ClientDao,
    private val cacheMapper : UserCacheMapper,
    private val clientCacheMapper : ClientCacheMapper
) : UserDaoService {

    override suspend fun insertUser(newUser: User): Long {
        return userDao.insertUser(
            cacheMapper.mapToEntity(newUser)
        )
    }

    override suspend fun deleteUser(userId: String): Int {
        return userDao.deleteUser(userId)
    }

    override suspend fun deleteUsers(): Int {
        return userDao.deleteUsers()
    }


    override suspend fun getUser(userId: String): User? {
        return userDao.getUser(userId)?.let{
            cacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun insertClient(newUser: User): Long {
        return clientDao.insertClient(
            clientCacheMapper.mapToEntity(newUser)
        )
    }

    override suspend fun deleteClient(): Int {
        return clientDao.deleteClient()
    }

    override suspend fun getClient(userId: String): User? {
        return clientDao.getClient(userId)?.let{
            clientCacheMapper.mapFromEntity(it)
        }
    }
}