package com.eahm.learn.framework.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.data.utils.GenericErrors
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewModel<ViewState>: ViewModel() {

    //region variables
    private val _viewState : MutableLiveData<ViewState> = MutableLiveData()

    val dataChannelManager : DataChannelManager<ViewState> = object: DataChannelManager<ViewState>(){
        override fun handleNewData(data: ViewState) {
            this@BaseViewModel.handleNewData(data)
        }
    }

    val viewState : LiveData<ViewState>
        get() = _viewState

    val shouldDisplayProgressBar : LiveData<Boolean> = dataChannelManager.shouldDisplayProgressBar

    val stateMessage: LiveData<StateMessage?>
        get() = dataChannelManager.messageStack.stateMessage

    //endregion variables

    abstract fun initNewViewState(): ViewState
    abstract fun handleNewData(data: ViewState)
    abstract fun setStateEvent(stateEvent: StateEvent)

    fun setupChannel() = dataChannelManager.setupChannel()

    fun clearChannel() = dataChannelManager.cancelJobs()

    fun getCurrentViewStateOrNew(): ViewState{
        return viewState.value ?: initNewViewState()
    }

    fun setViewState(viewState: ViewState){
        _viewState.value = viewState
    }

    fun emitStateMessageEvent(stateMessage: StateMessage, stateEvent: StateEvent) = flow{
        emit(
            DataState.error<ViewState>(
                response = stateMessage.response,
                stateEvent = stateEvent
            )
        )
    }

    fun emitInvalidStateEvent(stateEvent: StateEvent) = flow {
        emit(
            DataState.error<ViewState>(
                response = Response(
                    message = GenericErrors.INVALID_STATE_EVENT,
                    uiComponentType = UIComponentType.None(),
                    messageType = MessageType.Error()
                ),
                stateEvent = stateEvent
            )
        )
    }

    fun launchJob(
        stateEvent: StateEvent,
        jobFunction: Flow<DataState<ViewState>?>
    ) = dataChannelManager.launchJob(stateEvent, jobFunction)

    fun cancelActiveJobs() = dataChannelManager.cancelJobs()

    fun clearStateMessage(index: Int = 0){
        //printLogD("BaseViewModel", "clearStateMessage")
        dataChannelManager.clearStateMessage(index)
    }

    fun removeStateEvent(stateEvent : StateEvent) = dataChannelManager.removeStateEvent(stateEvent)

    fun clearActiveStateEvents() = dataChannelManager.clearActiveStateEventCounter()

    fun clearAllStateMessages() = dataChannelManager.clearAllStateMessages()

    fun printStateMessages() = dataChannelManager.printStateMessages()

    // FOR DEBUGGING
    fun getMessageStackSize(): Int{
        return dataChannelManager.messageStack.size
    }

}