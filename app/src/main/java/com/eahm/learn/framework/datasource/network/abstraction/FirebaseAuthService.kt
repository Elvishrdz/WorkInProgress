package com.eahm.learn.framework.datasource.network.abstraction

import com.eahm.learn.business.domain.model.User

interface FirebaseAuthService {
    suspend fun authenticateUser() : User
    suspend fun logout() : Boolean
}