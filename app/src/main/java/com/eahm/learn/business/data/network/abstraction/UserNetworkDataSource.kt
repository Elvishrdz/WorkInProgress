package com.eahm.learn.business.data.network.abstraction

import com.eahm.learn.business.domain.model.User

interface UserNetworkDataSource {
    suspend fun insertUser(newUser : User) : User?
//    suspend fun deleteUser(userId: String) : Int
    suspend fun setProviderId(userId : String, providerId: String) : Boolean
    suspend fun getUser(userId : String) : User?
}