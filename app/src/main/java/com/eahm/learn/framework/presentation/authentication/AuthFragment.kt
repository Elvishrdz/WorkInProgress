package com.eahm.learn.framework.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.eahm.learn.R
import com.eahm.learn.business.domain.factory.UserFactory
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.framework.presentation.common.BaseFragment
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import com.eahm.learn.utils.logD
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlinx.android.synthetic.main.layout_auth_login.*
import kotlinx.android.synthetic.main.layout_auth_register.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class AuthFragment
constructor(
    private val viewModelFactory : ViewModelProvider.Factory,
    private val userFactory: UserFactory
) : BaseFragment(layoutRes = R.layout.fragment_auth) {

    private val TAG = "AuthFragment"

    private val viewModel : AuthViewModel by viewModels{
        viewModelFactory
    }

    override fun inject() {
        getAppComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        subscribeObservers()
    }


    private fun setLoginView(){
        include_login.visibility = View.VISIBLE
        include_register.visibility = View.GONE
    }

    private fun setRegisterView(){
        include_register.visibility = View.VISIBLE
        include_login.visibility = View.GONE
    }

    private fun setupUI(){
        setLoginView()

        login_with_facebook.setOnClickListener{

        }

        login.setOnClickListener{
            //get user input and launch event
            //viewModel.setStateEvent()
            val user : String = input_username_email.editText?.text.toString()
            val pass  : String = input_password.editText?.text.toString()

            viewModel.authenticate(user, pass)
        }

        create_account.setOnClickListener{

            val name : String = reg_name.editText?.text.toString()
            val secondName : String = reg_second_name.editText?.text.toString()
            val lastName : String = reg_last_name.editText?.text.toString()
            val secondLastName : String = reg_second_last_name.editText?.text.toString()
            val email : String = reg_username_email.editText?.text.toString()
            val password : String = reg_password.editText?.text.toString()

            val newUser = userFactory.createUser(
                    id = "",
                    name = name,
                    secondName = secondName,
                    firstLastName = lastName,
                    secondLastName = secondLastName,
                    email = email
            )

            viewModel.createAccount(newUser, password)
        }

        login_to_register.setOnClickListener{ setRegisterView() }
        register_to_login.setOnClickListener{ setLoginView() }
    }

    private fun subscribeObservers() {
        viewModel.user.observe(viewLifecycleOwner, Observer{
            if(viewModel.isActiveSession && it != null){
                logD(TAG, "user is authenticated ${it.id}")

                //synchronizeUserData() // providers, shopping cart etc.
                navigateToUserProfile()
            }
        })

        viewModel.authMessage.observe(viewLifecycleOwner, Observer{ message ->
            if(message != null){
                logD(TAG, "Auth attempt Response: $message")

            }
        })
    }

    private fun displayCapturePassword(){

        /*uiController.displayInputCaptureDialog(
            getString(R.string.text_enter_password),
            object: DialogInputCaptureCallback {
                override fun onTextCaptured(text: String) {
                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(EMAIL, text)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                printLogD("MainActivity",
                                    "Signing in to Firebase: ${it.result}")
                                subscribeObservers()
                            }
                        }
                }
            }
        )*/



    }

    private fun navigateToUserProfile(){
        findNavController().navigate(R.id.action_authFragment_to_profileFragment)
    }

}