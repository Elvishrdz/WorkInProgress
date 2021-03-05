package com.eahm.learn.framework.presentation.profile

import androidx.lifecycle.LiveData
import com.eahm.learn.business.domain.factory.UserFactory
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.business.domain.state.DataState
import com.eahm.learn.business.domain.state.StateEvent
import com.eahm.learn.business.interactors.profile.ProfileInteractors
import com.eahm.learn.framework.presentation.common.BaseViewModel
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import com.eahm.learn.framework.presentation.profile.state.ProfileStateEvent.CreateUserProviderAccount
import com.eahm.learn.framework.presentation.profile.state.ProfileViewState
import com.eahm.learn.utils.logD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class ProfileViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val profileInteractors: ProfileInteractors,
    private val userFactory: UserFactory
): BaseViewModel<ProfileViewState>(){

    private val TAG = "ProfileViewModel"

    //region session access
    val user : LiveData<User?>
        get() = sessionManager.cachedUser

    val isActiveSession : Boolean
        get() = sessionManager.isActiveSession

    fun logout() = sessionManager.logout()
    //endregion session access

    //region view state
    override fun initNewViewState(): ProfileViewState = ProfileViewState()

    override fun handleNewData(data: ProfileViewState) {
        data.let { viewState ->
            viewState.updatedUser?.let {
                logD(TAG, "new user provider account received ${viewState.updatedUser?.providerId}")
                sessionManager.refreshSession()
                setUpdatedUser(it)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job : Flow<DataState<ProfileViewState>?> = when(stateEvent){

            is CreateUserProviderAccount -> {
                logD(TAG, "Create user provider account")
                profileInteractors.setUserToUserProvider.setUserToUserProvider(
                        provider = stateEvent.provider,
                        stateEvent = stateEvent
                )
            }

            else -> emitInvalidStateEvent(stateEvent)

        }

        launchJob(stateEvent, job)
    }
    //endregion view state

    private fun setUpdatedUser(user: User) {
        val update = getCurrentViewStateOrNew()
        update.updatedUser = user
        setViewState(update)
    }

    fun enableUserToUserProvider() {
        if(sessionManager.isActiveSession){
            val userId = sessionManager.cachedUser.value?.id
            logD(TAG, "enable user to user provider: $userId")

            userId?.let {
                if(userId.isNotEmpty()){
                    val user = userFactory.createUser(userId)

                    setStateEvent(
                        CreateUserProviderAccount(
                            provider = Provider(
                                id = "",
                                business = null,
                                user = user
                            )
                        )
                    )
                }
            }
        }
    }

    fun enableUserToBusinessProvider(){

    }



}