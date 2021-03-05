package com.eahm.learn.framework.presentation.orderlist.state

import com.eahm.learn.business.domain.model.Order
import com.eahm.learn.business.domain.state.ViewState

data class OrderListViewState(
    var orderList: List<Order>? = null
) : ViewState