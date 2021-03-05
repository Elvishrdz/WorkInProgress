package com.eahm.learn.framework.presentation

import android.app.Application
import com.eahm.learn.di.AppComponent
import com.eahm.learn.di.DaggerAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
open class BaseApplication : Application() {

    lateinit var appComponent : AppComponent

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    open fun initAppComponent(){
        appComponent = DaggerAppComponent.factory().create(this)
    }
}