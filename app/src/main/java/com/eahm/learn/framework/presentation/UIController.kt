package com.eahm.learn.framework.presentation

import com.eahm.learn.business.domain.state.DialogInputCaptureCallback
import com.eahm.learn.business.domain.state.Response
import com.eahm.learn.business.domain.state.StateMessageCallback


interface UIController {

    fun displayProgressBar(isDisplayed: Boolean)

    fun hideSoftKeyboard()

    fun displayInputCaptureDialog(title: String, callback: DialogInputCaptureCallback)

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

}
