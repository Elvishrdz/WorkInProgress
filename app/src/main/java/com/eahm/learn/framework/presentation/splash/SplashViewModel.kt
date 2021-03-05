package com.eahm.learn.framework.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import com.eahm.learn.framework.presentation.splash.manager.NetworkSyncManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashViewModel
@Inject constructor(
    val sessionManager: SessionManager,
    private val networkSyncManager : NetworkSyncManager
): ViewModel(){

    init {
        syncWithNetwork()
    }

    fun hasSyncBeenExecuted(): LiveData<Boolean>  = networkSyncManager.hasSyncBeenExecuted

    private fun syncWithNetwork() {
        networkSyncManager.executeDataSync(viewModelScope)
    }

}