package com.eahm.learn.framework.presentation.orderlistdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eahm.learn.R
import com.eahm.learn.business.domain.model.Order
import com.eahm.learn.framework.presentation.common.BaseFragment
import kotlinx.android.synthetic.main.order_list_detail_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class OrderListDetailFragment(
        private val viewModelFactory : ViewModelProvider.Factory
) : BaseFragment(layoutRes = R.layout.order_list_detail_fragment){


    private val viewModel : OrderListDetailViewModel by viewModels {
        viewModelFactory
    }

    override fun inject() {
        getAppComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOrderFromPreviousFragment()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.currentOrder.observe(viewLifecycleOwner, Observer {
            it?.let {
                setDetails(it)
            }
        })
    }

    private fun getOrderFromPreviousFragment(){
        arguments?.let { args ->
            (args.getParcelable(CURRENT_ORDER_BUNDLE_KEY) as Order?)?.let {
                viewModel.setCurrentOrder(it)
            }
        } //?: What to do if dont have value
    }

    private fun setDetails(currentOrder: Order) {
        order_details.text = currentOrder.products.toString()
    }

}