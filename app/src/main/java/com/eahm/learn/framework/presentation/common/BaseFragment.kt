package com.eahm.learn.framework.presentation.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.eahm.learn.business.domain.util.TodoCallback
import com.eahm.learn.di.AppComponent
import com.eahm.learn.framework.presentation.BaseApplication
import com.eahm.learn.framework.presentation.MainActivity
import com.eahm.learn.framework.presentation.UIController
import com.eahm.learn.framework.presentation.common.extensions.fadeIn
import com.eahm.learn.framework.presentation.common.extensions.fadeOut
import com.eahm.learn.framework.presentation.common.extensions.gone
import com.eahm.learn.framework.presentation.common.extensions.visible
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.lang.ClassCastException
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseFragment
constructor(
    @LayoutRes private val layoutRes: Int
): Fragment() {

    lateinit var uiController: UIController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    fun displayToolbarTitle(textView: TextView, title: String?, useAnimation: Boolean) {
        if(title != null){
            showToolbarTitle(textView, title, useAnimation)
        }
        else{
            hideToolbarTitle(textView, useAnimation)
        }
    }

    private fun hideToolbarTitle(textView: TextView, animation: Boolean){
        if(animation){
            textView.fadeOut(
                object: TodoCallback {
                    override fun execute() {
                        textView.text = ""
                    }
                }
            )
        }
        else{
            textView.text = ""
            textView.gone()
        }
    }

    private fun showToolbarTitle(
        textView: TextView,
        title: String,
        animation: Boolean
    ){
        textView.text = title
        if(animation){
            textView.fadeIn()
        }
        else{
            textView.visible()
        }
    }

    abstract fun inject()

    fun getAppComponent(): AppComponent {
        return activity?.run {
            (application as BaseApplication).appComponent
        }?: throw Exception("AppComponent is null.")
    }

    override fun onAttach(context: Context) {
        inject()
        super.onAttach(context)
        setUIController(null) // null in production
    }

    fun setUIController(mockController: UIController?){

        // TEST: Set interface from mock
        if(mockController != null){
            this.uiController = mockController
        }
        else{ // PRODUCTION: if no mock, get from context
            activity?.let {
                if(it is MainActivity){
                    try{
                        uiController = context as UIController
                    }catch (e: ClassCastException){
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}

















