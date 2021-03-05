package com.eahm.learn.framework.presentation.splash.manager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eahm.learn.business.interactors.splash.SyncProducts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkSyncManager
@Inject constructor(
    private val syncProducts: SyncProducts
//    private val syncPrices : SyncPrices
//    private val syncApplicationSettings : SyncApplicationSettings
) {

    private val _hasSyncBeenExecuted: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    val hasSyncBeenExecuted : LiveData<Boolean>
        get() = _hasSyncBeenExecuted

    fun executeDataSync(coroutineScope: CoroutineScope) {

        if(_hasSyncBeenExecuted.value!!) return

        val syncJob = coroutineScope.launch {

            /*val deletesJob = launch {
                syncProducts.syncProducts()
            }

            deletesJob.join()*/

            syncProducts.syncProducts()
        }

        syncJob.invokeOnCompletion {
            CoroutineScope(Main).launch {
                _hasSyncBeenExecuted.value = true
            }
        }



    }
}