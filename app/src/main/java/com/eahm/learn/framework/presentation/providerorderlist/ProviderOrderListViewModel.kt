package com.eahm.learn.framework.presentation.providerorderlist

import androidx.lifecycle.LiveData
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.business.domain.model.provider.OrderProvider
import com.eahm.learn.business.domain.state.DataState
import com.eahm.learn.business.domain.state.StateEvent
import com.eahm.learn.business.interactors.orderlistprovider.OrderListProviderInteractors
import com.eahm.learn.framework.presentation.common.BaseViewModel
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import com.eahm.learn.framework.presentation.providerorderlist.state.ProviderOrderListStateEvent.*
import com.eahm.learn.framework.presentation.providerorderlist.state.ProviderOrderListViewState
import com.eahm.learn.utils.logD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
@FlowPreview
class ProviderOrderListViewModel(
    private val sessionManager: SessionManager,
    private val orderListProviderInteractors : OrderListProviderInteractors
) : BaseViewModel<ProviderOrderListViewState>() {

    private val TAG = "OrderListProviderViewModel"

    //region baseviewmodel
    override fun initNewViewState(): ProviderOrderListViewState {
        return ProviderOrderListViewState()
    }

    override fun handleNewData(data: ProviderOrderListViewState) {
        data.let { viewState ->

            viewState.orderProviderList?.let {
                logD(TAG, "orderProviderList = ${it.size}")
                setOrderList(it)
            }

        }
    }

    private fun setOrderList(orderList: List<OrderProvider>) {
        val update = getCurrentViewStateOrNew()
        update.orderProviderList = orderList
        setViewState(update)
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        logD(TAG, "setStateEvent")
        val job : Flow<DataState<ProviderOrderListViewState>?> = when(stateEvent){
            is RetrieveOrders -> {
                logD(TAG, "RetrieveOrders")
                orderListProviderInteractors.retrieveOrdersAsProvider.retrieveOrders(
                        providerId = stateEvent.providerId,
                        stateEvent = stateEvent
                )
            }

            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }

        launchJob(stateEvent, job)
    }
    //endregion baseviewmodel

    //region session access
    val user : LiveData<User?>
        get() = sessionManager.cachedUser

    val isActiveSession : Boolean
        get() = sessionManager.isActiveSession

    //endregion session access

    override fun onCleared() {
        super.onCleared()
        sessionManager.onClearedSessionManager()
    }

    fun retrieveOrders() {
        logD(TAG, "retrieveOrders")
        if(sessionManager.isActiveSession){
            val currentProviderId = sessionManager.cachedUser.value?.id
            currentProviderId?.let {
                setStateEvent(
                    RetrieveOrders(
                            providerId = it
                    )
                )
            }
        }
    }

}