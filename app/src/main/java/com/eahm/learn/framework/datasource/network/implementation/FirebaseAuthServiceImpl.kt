package com.eahm.learn.framework.datasource.network.implementation

import com.eahm.learn.business.domain.model.User
import com.eahm.learn.framework.datasource.network.abstraction.FirebaseAuthService
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthServiceImpl(
    private val firebaseAuth : FirebaseAuth
) : FirebaseAuthService {

    override suspend fun authenticateUser(): User {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Boolean {
        TODO("Not yet implemented")
    }
}