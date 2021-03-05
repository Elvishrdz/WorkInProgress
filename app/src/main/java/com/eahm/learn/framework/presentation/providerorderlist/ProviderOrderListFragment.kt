package com.eahm.learn.framework.presentation.providerorderlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eahm.learn.R
import com.eahm.learn.framework.presentation.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_order_list_provider.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class ProviderOrderListFragment(
    private val viewModelFactory : ViewModelProvider.Factory
) : BaseFragment(
    layoutRes = R.layout.fragment_order_list_provider
) {

    private val adapter = ProviderOrderAdapter()

    private val viewModel : ProviderOrderListViewModel by viewModels{
        viewModelFactory
    }

    override fun inject() {
        getAppComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*setupUI()*/
        setupRecyclerView()
        subscribeObservers()
    }

    private fun setupRecyclerView() {
        order_list.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        order_list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.clearAllStateMessages()
        viewModel.retrieveOrders()
    }

    private fun subscribeObservers() {
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if(user != null && viewModel.isActiveSession){
                // show Authenticated view

            }
            else {
                // show NotAuthenticated view

            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if(viewState != null){
                viewState.orderProviderList?.let {
                    adapter.submitList(it)
                }
            }
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { message ->
            if(message != null){

            }
        })
    }

}