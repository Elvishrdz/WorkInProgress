package com.eahm.learn.framework.presentation.orderlist

import androidx.lifecycle.LiveData
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.business.domain.model.Order
import com.eahm.learn.business.domain.state.DataState
import com.eahm.learn.business.domain.state.StateEvent
import com.eahm.learn.business.interactors.order.OrderListInteractors
import com.eahm.learn.framework.presentation.common.BaseViewModel
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import com.eahm.learn.framework.presentation.orderlist.state.OrderListViewState
import com.eahm.learn.framework.presentation.providerorderlist.state.ProviderOrderListStateEvent.*
import com.eahm.learn.utils.logD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
@FlowPreview
class OrderListViewModel(
    private val sessionManager: SessionManager,
    private val orderListInteractors : OrderListInteractors
) : BaseViewModel<OrderListViewState>() {

    private val TAG = "OrderListViewModel"

    //region baseviewmodel
    override fun initNewViewState(): OrderListViewState {
        return OrderListViewState()
    }

    override fun handleNewData(data: OrderListViewState) {
        data.let { viewState ->

            viewState.orderList?.let {
                logD(TAG, "orderProviderList = ${it.size}")
                setOrderList(it)
            }

        }
    }

    private fun setOrderList(orderList: List<Order>) {
        val update = getCurrentViewStateOrNew()
        update.orderList = orderList
        setViewState(update)
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        if(sessionManager.isActiveSession){
            logD(TAG, "order list set state event")
            val job : Flow<DataState<OrderListViewState>?> = when(stateEvent){

                is RetrieveOrders -> {
                    val userId = sessionManager.cachedUser.value?.id ?: ""
                    logD(TAG, "retrieve orders of $userId")
                    orderListInteractors.retrieveOrders.retrieveOrders(
                            userId = userId,
                            stateEvent = stateEvent
                    )
                }

                else -> {
                    emitInvalidStateEvent(stateEvent)
                }
            }
            launchJob(stateEvent, job)
        }
        else {
            //todo emit a message that require auth
            logD(TAG, "order list state event require auth")
            launchJob(
                    stateEvent,
                    emitInvalidStateEvent(stateEvent)
            )
        }
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