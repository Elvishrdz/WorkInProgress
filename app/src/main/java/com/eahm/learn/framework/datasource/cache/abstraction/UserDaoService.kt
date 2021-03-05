package com.eahm.learn.framework.datasource.cache.abstraction

import com.eahm.learn.business.domain.model.User

interface UserDaoService {
    suspend fun insertUser(newUser : User) : Long
    suspend fun deleteUser(userId: String) : Int
    suspend fun deleteUsers() : Int
    suspend fun getUser(userId : String) : User?

    suspend fun insertClient(newUser : User) : Long
    suspend fun deleteClient() : Int
    suspend fun getClient(userId : String) : User?
}