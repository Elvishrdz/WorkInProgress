package com.eahm.learn.framework.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eahm.learn.R
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.framework.presentation.common.BaseFragment
import com.eahm.learn.utils.logD
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@FlowPreview
class ProfileFragment constructor (
        private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(R.layout.fragment_profile) {

    private val TAG = "ProfileFragment"

    override fun inject() {
        getAppComponent().inject(this)
    }

    private val viewModel: ProfileViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        subscribeObservers()
    }

    private fun setupUI() {
        become_a_provider.setOnClickListener{
            viewModel.enableUserToUserProvider()
        }

        user_logout.setOnClickListener{
            viewModel.logout()
        }
    }

    private fun subscribeObservers() {
        viewModel.user.observe(viewLifecycleOwner, Observer {
            logD(TAG, "Current user: '$it'")
            if(viewModel.isActiveSession && it != null){

                checkUserProvider(it)

                if(it.name_first.isNotEmpty()){
                    profile_user_name.text = "${it.name_first} ${it.name_second} ${it.last_name_first} ${it.last_name_second}"
                }

            }
            else {
                logD(TAG, "No active session here")
                navigateToAuth()
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if(viewState != null){
                viewState.updatedUser?.let {
                    logD(TAG, "User updated: $it")
                    checkUserProvider(it)
                }
            }
        })
    }

    private fun checkUserProvider(currentUser: User) {
        if(currentUser.providerId.isNotEmpty()){
            hideBecomeAProvider()
        }
        else {
            showBecomeAProvider()
        }
    }

    private fun showBecomeAProvider(){
        become_a_provider.visibility = View.VISIBLE
    }

    private fun hideBecomeAProvider(){
        become_a_provider.visibility = View.GONE
    }

    private fun navigateToAuth(){
        findNavController().navigate(R.id.action_profileFragment_to_authFragment)
    }
}