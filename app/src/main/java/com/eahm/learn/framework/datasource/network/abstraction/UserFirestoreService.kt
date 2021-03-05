package com.eahm.learn.framework.datasource.network.abstraction

import com.eahm.learn.business.domain.model.User

interface UserFirestoreService {
    suspend fun insertUser(newUser : User) : User?
//    suspend fun deleteUser(userId: String) : Int
    suspend fun setProviderId(userId : String, providerId: String) : Boolean
    suspend fun getUser(userId : String) : User?
}