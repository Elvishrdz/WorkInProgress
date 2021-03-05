package com.eahm.learn.framework.presentation.product

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.model.ProductImage
import com.eahm.learn.business.domain.model.ProductTechnicalInfo
import com.eahm.learn.R
import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.framework.presentation.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class ProductFragment
constructor(
        private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(layoutRes = R.layout.fragment_product) {

    private val viewModel : ProductViewModel by viewModels{
        viewModelFactory
    }

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
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.sessionManager.cachedUser.observe(viewLifecycleOwner, Observer { currentUser ->
            if(currentUser != null){
                insert_product_title.text = "${currentUser.name_first} ${currentUser.name_second}"
            }
            else {
                insert_product_title.text = "Add a new product:"
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                viewState.currentProduct?.let {
                    Toast.makeText(context, "Current! ${it.title}", Toast.LENGTH_SHORT).show()
                }
                viewState.lastPublishedProduct?.let {
                    Toast.makeText(context, "Agregated! ${it.title}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer {

        })
    }

    private fun setupUI() {
        new_product_title.editText?.setText("")
        new_product_description.editText?.setText("")
        new_product_price.editText?.setText("")

        showLocalLoadingView(false)

        new_product_publish.setOnClickListener{
            insertNewProduct()
        }
    }

    private fun insertNewProduct() {
        if(!isCurrentProductValid()){
            Toast.makeText(context, "Complete all fields", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.setCurrentNewProduct(
            new_product_title.editText?.text.toString(),
            new_product_description.editText?.text.toString(),
            new_product_price.editText?.text.toString().toFloat()
        )
        viewModel.insertNewProduct()

    }

    private fun isCurrentProductValid(): Boolean {
        return (
            new_product_title.editText?.text?.isNotEmpty() == true &&
            new_product_description.editText?.text?.isNotEmpty() == true &&
            new_product_price.editText?.text?.isNotEmpty() == true
        )
    }

    private fun showLocalLoadingView(setVisible: Boolean){
        new_product_loading.visibility = if(setVisible) View.VISIBLE else View.GONE
    }



}