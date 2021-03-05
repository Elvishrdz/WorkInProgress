package com.eahm.learn.framework.presentation.orderlist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eahm.learn.R
import com.eahm.learn.business.domain.model.Order
import com.eahm.learn.framework.presentation.common.BaseFragment
import com.eahm.learn.framework.presentation.orderlistdetail.CURRENT_ORDER_BUNDLE_KEY
import kotlinx.android.synthetic.main.fragment_order_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class OrderListFragment(
        private val viewModelFactory : ViewModelProvider.Factory
) : BaseFragment(
        layoutRes = R.layout.fragment_order_list
) {

    private var adapter: OrderAdapter? = null

    private val viewModel : OrderListViewModel by viewModels{
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
        orders.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        val interaction =  object : OrderInteractions{
            override fun onItemSelected(order: Order) {
                navigateToOrderListDetailFragment(order)
            }
        }
        adapter = OrderAdapter(interaction)
        orders.adapter = adapter
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
                viewState.orderList?.let {
                    adapter?.submitList(it)
                }
            }
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { message ->
            if(message != null){

            }
        })
    }

    fun navigateToOrderListDetailFragment(order : Order){
        val bundle = bundleOf(CURRENT_ORDER_BUNDLE_KEY to order)
        findNavController().navigate(R.id.action_orderListFragment_to_orderListDetailFragment, bundle)
    }

}