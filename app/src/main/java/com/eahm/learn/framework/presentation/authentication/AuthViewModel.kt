package com.eahm.learn.framework.presentation.authentication

import androidx.lifecycle.LiveData
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.business.domain.state.StateEvent
import com.eahm.learn.framework.presentation.authentication.state.AuthViewState
import com.eahm.learn.framework.presentation.common.BaseViewModel
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class AuthViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
) : BaseViewModel<AuthViewState>() {

    //region session access
    val user : LiveData<User?>
        get() = sessionManager.cachedUser

    val isActiveSession : Boolean
        get() = sessionManager.isActiveSession

    val authMessage : LiveData<String?>
        get() = sessionManager.authMessage
    //endregion session access

    override fun initNewViewState(): AuthViewState = AuthViewState()

    override fun handleNewData(data: AuthViewState) {

    }

    override fun setStateEvent(stateEvent: StateEvent) {

    }

    fun authenticate(user: String, pass: String) {
        sessionManager.authenticate(user, pass)
    }

    fun createAccount(newUser: User, password: String) {
        sessionManager.createAccount(newUser, password)
    }
}