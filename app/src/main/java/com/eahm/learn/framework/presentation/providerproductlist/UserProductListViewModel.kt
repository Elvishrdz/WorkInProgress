package com.eahm.learn.framework.presentation.providerproductlist

import androidx.lifecycle.LiveData
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.business.domain.state.DataState
import com.eahm.learn.business.domain.state.StateEvent
import com.eahm.learn.business.interactors.userproductlist.UserProductListInteractors
import com.eahm.learn.framework.presentation.common.BaseViewModel
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import com.eahm.learn.framework.presentation.providerproductlist.state.UserProductListStateEvent.RetrieveUserProducts
import com.eahm.learn.framework.presentation.providerproductlist.state.UserProductListViewState
import com.eahm.learn.utils.logD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
@ExperimentalCoroutinesApi
@FlowPreview
@Singleton
class UserProductListViewModel
@Inject
constructor(
        private val sessionManager: SessionManager,
        private val userProductListInteractors: UserProductListInteractors
): BaseViewModel<UserProductListViewState>(){

    private val TAG = "UserProductListViewModel"

    //region session access
    val user : LiveData<User?>
        get() = sessionManager.cachedUser

    val isActiveSession : Boolean
        get() = sessionManager.isActiveSession

    //endregion session access

    override fun initNewViewState(): UserProductListViewState {
        return UserProductListViewState()
    }

    override fun handleNewData(data: UserProductListViewState) {
        data.let { viewState ->
            viewState.userProductList?.let {
                setUserProducList(it)
            }

        }
    }

    private fun setUserProducList(productList: List<Product>) {
        val update = getCurrentViewStateOrNew()
        update.userProductList = productList
        setViewState(update)
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job : Flow<DataState<UserProductListViewState>> = when(stateEvent){

            is RetrieveUserProducts ->{
                val providerId = sessionManager.cachedUser.value?.providerId ?: ""
                logD(TAG, "retrieve user products from $providerId")
                userProductListInteractors.getProviderProducts.getProviderProducts(
                        providerId = providerId,
                        stateEvent = stateEvent
                )
            }

            else ->{
                emitInvalidStateEvent(stateEvent)
            }
        }

        launchJob(stateEvent, job)
    }

    fun retrieveUserProducts() {
        setStateEvent(RetrieveUserProducts())
    }


}

