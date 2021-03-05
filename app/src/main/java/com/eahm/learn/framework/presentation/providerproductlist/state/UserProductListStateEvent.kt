package com.eahm.learn.framework.presentation.providerproductlist.state

import com.eahm.learn.business.domain.state.StateEvent

sealed class UserProductListStateEvent : StateEvent {

    class RetrieveUserProducts() : UserProductListStateEvent() {
        override fun errorInfo(): String = "Couldnt retrieve user products"

        override fun eventName(): String = "retrieveUserProducts"

        override fun shouldDisplayProgressBar(): Boolean = true

    }
}