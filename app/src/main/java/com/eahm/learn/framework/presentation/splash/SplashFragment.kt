package com.eahm.learn.framework.presentation.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eahm.learn.R
import com.eahm.learn.framework.presentation.common.BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
class SplashFragment
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
): BaseFragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModels {
        viewModelFactory
    }

    override fun inject() {
        getAppComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkFirebaseAuth()
    }

    private fun checkFirebaseAuth(){
        /*
        Here obtain the firebase settings and send the user credential if theres
        a user logged in.
         */
        /*if(FirebaseAuth.getInstance().currentUser == null){
            displayCapturePassword()
        }
        else{
            subscribeObservers()
        }*/

        subscribeObservers()
    }

    private fun subscribeObservers(){
        viewModel.hasSyncBeenExecuted().observe(viewLifecycleOwner, Observer { hasSyncBeenExecuted ->
            if(hasSyncBeenExecuted){
                navigateToProductListFragment()
            }
        })

        viewModel.sessionManager.cachedUser.observe(viewLifecycleOwner, Observer {
             // check if theres a user
            if(viewModel.sessionManager.isActiveSession){
                // prepare auth UI
            }
        })
    }

    private fun navigateToProductListFragment(){
        findNavController().navigate(R.id.action_splashFragment_to_productListFragment)
    }

}