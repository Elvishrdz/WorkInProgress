package com.eahm.learn.framework.presentation.providerproductlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eahm.learn.R
import com.eahm.learn.framework.presentation.common.BaseFragment
import com.eahm.learn.utils.logD
import kotlinx.android.synthetic.main.fragment_user_product_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@ExperimentalCoroutinesApi
@FlowPreview
class UserProductListFragment
constructor(
        private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(layoutRes = R.layout.fragment_user_product_list) {

    private val TAG = "UserProductListFragment"

    private val viewModel : UserProductListViewModel by viewModels{
        viewModelFactory
    }

    private val adapter = UserProductListAdapter()

    override fun inject() {
        getAppComponent().inject(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupRecyclerView()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.clearAllStateMessages()
        viewModel.retrieveUserProducts()
    }

    private fun setupUI(){
        new_product.setOnClickListener{
            navigateToProduct()
        }
    }

    private fun setupRecyclerView(){
        user_product_list.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        user_product_list.adapter = adapter

    }

    private fun subscribeObservers(){

        viewModel.user.observe(viewLifecycleOwner, Observer {
            logD(TAG, "user status: ${viewModel.isActiveSession} - $it")
            if(viewModel.isActiveSession && it != null){

            }
            else {

            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer{ viewState ->
            if(viewState != null){
                viewState.userProductList?.let {
                    adapter.submitList(it)
                }
            }
        })
    }

    fun navigateToProduct(){
        findNavController().navigate(R.id.action_userProductListFragment_to_productFragment)
    }


}