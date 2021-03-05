package com.eahm.learn.business.data.network.implementation

import com.eahm.learn.business.data.network.abstraction.UserNetworkDataSource
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.framework.datasource.network.abstraction.UserFirestoreService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserNetworkDataSourceImpl
@Inject constructor(
    private val firestoreService : UserFirestoreService
) : UserNetworkDataSource {

    override suspend fun insertUser(newUser : User) : User? {
        return firestoreService.insertUser(newUser)
    }

    override suspend fun setProviderId(userId : String, providerId: String): Boolean {
        return firestoreService.setProviderId(userId, providerId)
    }

    override suspend fun getUser(userId: String): User? {
        return firestoreService.getUser(userId)
    }
}