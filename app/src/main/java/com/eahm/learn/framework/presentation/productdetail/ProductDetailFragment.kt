package com.eahm.learn.framework.presentation.productdetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.R
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.interactors.common.InsertProductInCart.Companion.SHOPPING_CART_INSERT_FAIL
import com.eahm.learn.business.interactors.shoppingcart.SearchProductInCart
import com.eahm.learn.business.interactors.shoppingcart.SearchProductInCart.Companion.SHOPPING_CART_SEARCH_FAILED
import com.eahm.learn.business.interactors.shoppingcart.SearchProductInCart.Companion.SHOPPING_CART_SEARCH_NOT_FOUNDED
import com.eahm.learn.framework.presentation.common.extensions.*
import com.eahm.learn.framework.presentation.common.BaseFragment
import com.eahm.learn.framework.presentation.common.CURRENCY
import com.eahm.learn.framework.presentation.productdetail.state.ProductDetailViewState
import com.eahm.learn.framework.presentation.productlist.state.ProductListStateEvent.*
import com.eahm.learn.utils.logD
import com.infinum.dbinspector.DbInspector
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.layout_provider_small_information.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

const val PRODUCT_DETAIL_STATE_BUNDLE_KEY
= "com.eahm.learn.products.framework.presentation.productdetail.state"

@FlowPreview
@ExperimentalCoroutinesApi
class ProductDetailFragment
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
): BaseFragment(R.layout.fragment_product_detail) {

    private val TAG = "ProductDetailFragment"

    val viewModel: ProductDetailViewModel by viewModels {
        viewModelFactory
    }
    private var rootView : View? = null

    override fun inject() {
        getAppComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView = view

        setupUI()
        setupOnBackPressDispatcher()
        subscribeObservers()

        restoreInstanceState()
    }

    override fun onResume() {
        super.onResume()
        logD(TAG, "onResume Called")
        getSelectedProductFromPreviousFragment()
    }
    


    private fun setupUI(){
        detail_provider.visibility = View.GONE
        hideCartOptions()

        btn_add_to_cart.setOnClickListener{
            // todo we should clear the stack of messages in the close button of the dialogMessage view
            viewModel.clearAllStateMessages() // if we are showing a message on screen, close it
            viewModel.addCurrentProductToShoppingCart()
        }

        btn_go_to_cart.setOnClickListener{
            navigateToShoppingCart()
        }

        btn_db.setOnClickListener{
            DbInspector.show()
        }
    }

    //region navigation
    private fun navigateToShoppingCart(){
        findNavController().navigate(R.id.action_productDetailFragment_to_shoppingCartFragment)
    }

    private fun navigateToAuth(){
        findNavController().navigate(R.id.action_productDetailFragment_to_authFragment)
    }
    //endregion navigation


    private fun restoreInstanceState(){
        arguments?.let { args ->
            (args.getParcelable(PRODUCT_DETAIL_STATE_BUNDLE_KEY) as ProductDetailViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    private fun getSelectedProductFromPreviousFragment(){
        arguments?.let { args ->
            (args.getParcelable(PRODUCT_DETAIL_SELECTED_PRODUCT_BUNDLE_KEY) as Product?)?.let { selectedProduct ->
                logD(TAG, "Set product and check in shopping cart")
                viewModel.setProduct(selectedProduct)
            }?: onErrorRetrievingProductFromPreviousFragment()
        }
    }

    private fun onErrorRetrievingProductFromPreviousFragment(){
        viewModel.setStateEvent(
            CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = PRODUCT_DETAIL_ERROR_RETRIEVEING_SELECTED_PRODUCT,
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    )
                )
            )
        )
    }

    private fun setupOnBackPressDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun onBackPressed() {
        view?.hideKeyboard()
        findNavController().popBackStack()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.getCurrentViewStateOrNew()
        outState.putParcelable(PRODUCT_DETAIL_STATE_BUNDLE_KEY, viewState)
        super.onSaveInstanceState(outState)
    }

    //region setters


    private fun setProductDetails(product: Product) {
        detail_title.text = product.title

        rootView?.let {
            if(product.photos.isNotEmpty()){
                Glide
                    .with(it)
                    .load(product.photos[0].photoUrl)
                    .centerInside()
                    .into(detail_photos)
            }
        }

        detail_price.text = "${product.price} $CURRENCY"
        detail_description.text = product.description
    }

    private fun setProductProviderInformation(provider : Provider){
        if(provider.business != null && provider.business.isValid()){
            setProviderName(provider.business.name)
            showProviderInformation(true)
        }
        else if(provider.user != null && provider.user.isValid()) {
            setProviderName("${provider.user.name_first} ${provider.user.last_name_first}")
            showProviderInformation(true)
        }
        else showProviderInformation(false)
    }

    private fun setProviderName(providerName : String){
        detail_provider.provider_public_name.text = providerName
    }

    private fun showProviderInformation(status :Boolean){
        detail_provider.visibility = if(status) View.VISIBLE else View.GONE
    }


    //endregion setters

    private fun subscribeObservers(){
        viewModel.user.observe(viewLifecycleOwner, Observer {
            if(it != null){
                //viewModel.setCurrentProvider(it.providerId)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {

                viewState.product?.let { product ->
                    logD(TAG, "View state, product called ${product.id}")
                    setProductDetails(product)
                    viewModel.checkProductInShoppingCart() // Set Button in ui if the product is in the cart
                    getProviderInformation()

                }

                viewState.productInCart?.let { productInTheShoppingCart ->
                    logD(TAG, "View state, product in cart called. Amount: ${productInTheShoppingCart.amount}")
                    if(productInTheShoppingCart.amount > 0) showGoToCart()

                    // Always check if the product is still in the shopping cart
                    viewModel.setProductInCart(null)
                }

                viewState.provider?.let {
                    // WE HAVE INFORMATION. SET DEPENDING ON THE PROVIDER BUSINESS OR USER
                    // AND SAVE THIS INFO IN BUNDLE. CHECK IT BEFORE YOU OBTAIN THE PROVIDER
                    // INFORMATION AGAIN. AND RESTORE IT EVERYTIME IF USER STAYS IN THE FRAGMENT.
                    logD(TAG, "Provider of the product: ${it.business?.name} ${it.user?.name_first}")
                    setProductProviderInformation(it)
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer {
            uiController.displayProgressBar(it)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            logD(TAG, "Check state message ${stateMessage.toString()}")
            stateMessage?.response?.let { response ->
                logD(TAG, "Response: ${response.message}")

                if(response.message?.contains(SHOPPING_CART_SEARCH_NOT_FOUNDED) == true){
                    logD(TAG, "DO ACTIONS")
                    showAddToCart()
                }

                when(response.message){

                    SHOPPING_CART_SEARCH_NOT_FOUNDED -> {
                        logD(TAG, "show button")
                        showAddToCart()
                    }

                    SHOPPING_CART_SEARCH_FAILED -> {
                        showAddToCart()
                    }

                    SHOPPING_CART_INSERT_FAIL ->{
                        showAddToCart()
                    }

                    /*   DELETE_PRODUCT_SUCCESS -> {
                           viewModel.clearStateMessage()
                           onDeleteSuccess()
                       }*/

                    else -> {
                        uiController.onResponseReceived(
                            response = stateMessage.response,
                            stateMessageCallback = object: StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            }
                        )

                        when(response.message){

                            /*UPDATE_PRODUCT_FAILED_PK -> {
                                findNavController().popBackStack()
                            }*/
/*
                            PRODUCT_DETAIL_ERROR_RETRIEVEING_SELECTED_PRODUCT -> {
                                findNavController().popBackStack()
                            }*/

                            else -> {
                                // do nothing
                            }
                        }
                    }
                }
            }

        })
        
    }

    private fun showAddToCart(){
        btn_go_to_cart.visibility = View.GONE
        btn_add_to_cart.visibility = View.VISIBLE
    }

    private fun showGoToCart(){
        btn_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.VISIBLE
    }

    private fun hideCartOptions(){
        btn_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.GONE
    }

    private fun getProviderInformation() {
        viewModel.getProviderInformation()
    }


}
