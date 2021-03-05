package com.eahm.learn.framework.presentation.orderlist.state

import com.eahm.learn.business.domain.state.StateEvent

sealed class OrderListStateEvent : StateEvent {

    class RetrieveOrders : OrderListStateEvent() {
        override fun errorInfo(): String = "Couldnt retrieve current orders"

        override fun eventName(): String = "RetrieveOrders"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

}