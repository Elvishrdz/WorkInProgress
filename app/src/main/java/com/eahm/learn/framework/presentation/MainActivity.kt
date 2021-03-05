package com.eahm.learn.framework.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.domain.util.TodoCallback
import com.eahm.learn.R
import com.eahm.learn.business.domain.state.UIComponentType.*
import com.eahm.learn.framework.presentation.common.FragmentFactory
import com.eahm.learn.framework.presentation.common.extensions.displayToast
import com.eahm.learn.framework.presentation.common.extensions.hideKeyboard
import com.eahm.learn.utils.logD
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject


@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity : AppCompatActivity(), UIController {

    private val TAG = "MainActivity"

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setFragmentFactory()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    private fun inject(){
        (application as BaseApplication).appComponent.inject(this)
    }

    private fun setFragmentFactory(){
        supportFragmentManager.fragmentFactory = fragmentFactory
    }


    //region UIController
    override fun displayProgressBar(isDisplayed: Boolean) {
          main_loading.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

    override fun hideSoftKeyboard() {
        hideKeyboard()
    }

    override fun displayInputCaptureDialog(title: String, callback: DialogInputCaptureCallback) {
        /*dialogInView = MaterialDialog(this).show {
            title(text = title)
            input(
                    waitForPositiveButton = true,
                    inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            ){ _, text ->
                callback.onTextCaptured(text.toString())
            }
            positiveButton(R.string.text_ok)
            onDismiss {
                dialogInView = null
            }
            cancelable(true)
        }*/
    }

    override fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {
        when(response.uiComponentType){

            is SnackBar -> {
                val onDismissCallback: TodoCallback? = response.uiComponentType.onDismissCallback
                val undoCallback: SnackbarUndoCallback? = response.uiComponentType.undoCallback
                response.message?.let { msg ->
                    displaySnackbar(
                            message = msg,
                            snackbarUndoCallback = undoCallback,
                            onDismissCallback = onDismissCallback,
                            stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is AreYouSureDialog -> {
                response.message?.let {
                   /* dialogInView = areYouSureDialog(
                            message = it,
                            callback = response.uiComponentType.callback,
                            stateMessageCallback = stateMessageCallback
                    )*/
                }
            }

            is Toast -> {
                response.message?.let {
                    displayToast(
                            message = it,
                            stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is Dialog -> {
                displayDialog(
                        response = response,
                        stateMessageCallback = stateMessageCallback
                )
            }

            is None -> {
                logD(TAG, "onResponseReceived: ${response.message}")
                stateMessageCallback.removeMessageFromStack()
            }
        }
    }

    //endregion UIController

    override fun onBackPressed() {
        super.onBackPressed()
    }

    //region view
    private fun displaySnackbar(
            message: String,
            snackbarUndoCallback: SnackbarUndoCallback?,
            onDismissCallback: TodoCallback?,
            stateMessageCallback: StateMessageCallback
    ){
        val snackbar = Snackbar.make(
            findViewById(R.id.main),
            message,
            Snackbar.LENGTH_LONG
        )
        /*snackbar.setAction(
            getString(R.string.text_undo),
            SnackbarUndoListener(snackbarUndoCallback)
        )*/
        snackbar.addCallback(object: BaseTransientBottomBar.BaseCallback<Snackbar>(){
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                onDismissCallback?.execute()
            }
        })
        snackbar.show()
        stateMessageCallback.removeMessageFromStack()
    }
    //private var dialogInView: MaterialDialog? = null


    private fun displayDialog(
            response: Response,
            stateMessageCallback: StateMessageCallback
    ){
        response.message?.let { message ->

           /* dialogInView = when (response.messageType) {

                is MessageType.Error -> {
                    displayErrorDialog(
                            message = message,
                            stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Success -> {
                    displaySuccessDialog(
                            message = message,
                            stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Info -> {
                    displayInfoDialog(
                            message = message,
                            stateMessageCallback = stateMessageCallback
                    )
                }

                else -> {
                    // do nothing
                    stateMessageCallback.removeMessageFromStack()
                    null
                }
            }*/

        }?: stateMessageCallback.removeMessageFromStack()
    }

    /*private fun displaySuccessDialog(
            message: String?,
            stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
                .show{
                    title("Sucessfully Completed!") //R.string.text_success
                    message(text = message)
                    positiveButton(R.string.text_ok){
                        stateMessageCallback.removeMessageFromStack()
                        dismiss()
                    }
                    onDismiss {
                        dialogInView = null
                    }
                    cancelable(false)
                }
    }

    private fun displayErrorDialog(
            message: String?,
            stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
                .show{
                    title(R.string.text_error)
                    message(text = message)
                    positiveButton(R.string.text_ok){
                        stateMessageCallback.removeMessageFromStack()
                        dismiss()
                    }
                    onDismiss {
                        dialogInView = null
                    }
                    cancelable(false)
                }
    }

    private fun displayInfoDialog(
            message: String?,
            stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
                .show{
                    title(R.string.text_info)
                    message(text = message)
                    positiveButton(R.string.text_ok){
                        stateMessageCallback.removeMessageFromStack()
                        dismiss()
                    }
                    onDismiss {
                        dialogInView = null
                    }
                    cancelable(false)
                }
    }

    private fun areYouSureDialog(
            message: String,
            callback: AreYouSureCallback,
            stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
                .show{
                    title(R.string.are_you_sure)
                    message(text = message)
                    negativeButton(R.string.text_cancel){
                        stateMessageCallback.removeMessageFromStack()
                        callback.cancel()
                        dismiss()
                    }
                    positiveButton(R.string.text_yes){
                        stateMessageCallback.removeMessageFromStack()
                        callback.proceed()
                        dismiss()
                    }
                    onDismiss {
                        dialogInView = null
                    }
                    cancelable(false)
                }
    }*/

    //endregion view

}


/*
    1. Crear modulo buildSrc para el manejo de dependencias en todos los modulos.
    2. Acabo de crear los modulos business, domain, framework y util.
    3. Crear directorio de carpetas en el modulo business

 */
