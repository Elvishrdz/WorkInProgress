package com.eahm.learn.framework.presentation.providerorderlist.state

import com.eahm.learn.business.domain.state.StateEvent

sealed class ProviderOrderListStateEvent : StateEvent {

    class RetrieveOrders(
        val providerId : String
    ) : ProviderOrderListStateEvent() {
        override fun errorInfo(): String = "Couldnt retrieve current orders"

        override fun eventName(): String = "RetrieveOrders"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

}