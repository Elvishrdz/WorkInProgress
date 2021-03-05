package com.eahm.learn.framework.presentation.profile.state

import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.state.StateEvent

sealed class ProfileStateEvent : StateEvent {

    class CreateUserProviderAccount(
        val provider : Provider
    ) : ProfileStateEvent() {
        override fun errorInfo(): String = "enabling provider account failed."
        override fun eventName(): String = "CreateProviderAccount"
        override fun shouldDisplayProgressBar(): Boolean = false

    }
}