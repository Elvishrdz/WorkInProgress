package com.eahm.learn.framework.presentation.providerorderlist.state

import com.eahm.learn.business.domain.model.provider.OrderProvider
import com.eahm.learn.business.domain.state.ViewState

data class ProviderOrderListViewState(
    var orderProviderList: List<OrderProvider>? = null
) : ViewState