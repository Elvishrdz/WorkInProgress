package com.eahm.learn.framework.presentation.shoppingcart

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.business.domain.state.StateMessageCallback
import com.eahm.learn.R
import com.eahm.learn.business.interactors.shoppingcart.GetAllItems.Companion.SHOPPING_CART_GET_ALL_FAILED
import com.eahm.learn.framework.presentation.common.BaseFragment
import com.eahm.learn.framework.presentation.common.CURRENCY
import com.eahm.learn.framework.presentation.common.decoration.TopSpacingItemDecoration
import com.eahm.learn.framework.presentation.shoppingcart.adapter.ShoppingCartAdapter
import com.eahm.learn.utils.logD
import kotlinx.android.synthetic.main.fragment_shopping_cart.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class ShoppingCartFragment
constructor(
    private val viewModelFactory : ViewModelProvider.Factory
) : BaseFragment(layoutRes = R.layout.fragment_shopping_cart),
ShoppingCartAdapter.Interaction{

    //region variables
    private val TAG = "ShoppingCartFragment"

    private val viewModel : ShoppingCartViewModel by viewModels{
        viewModelFactory
    }

    private lateinit var listAdapter: ShoppingCartAdapter
    //endregion variables

    override fun inject() {
        getAppComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setFilter(Filter("ALL"))
        setupUI()
        setupRecyclerView()
        subscribeObservers()
    }

    private fun setupUI() {
        delivery_check.setOnCheckedChangeListener { buttonView, isChecked ->
            deliveryPrice = if(isChecked) 30 else 0
            calculatePaymentInformation()
        }

        btn_cart_pay_now.setOnClickListener{
            viewModel.placeOrder()
        }
    }

    private fun setupRecyclerView() {
        cart_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)

            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)

            listAdapter = ShoppingCartAdapter(viewLifecycleOwner, this@ShoppingCartFragment)

            adapter = listAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getShoppingCartItemAmountNum()
        viewModel.getCurrentShoppingCartList()
    }

    private fun subscribeObservers() {
        viewModel.user.observe(viewLifecycleOwner, Observer{
            if(viewModel.isActiveSession && it != null){

            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer{viewState ->
            if(viewState != null){
                viewState.itemsList?.let { shoppingList ->
                    logD(TAG, "Items in list: ${shoppingList.size}")

                    listAdapter.submitList(shoppingList)

                    if(shoppingList.size > 0){
                        hideEmptyCartView()
                        calculatePaymentInformation()
                    }
                    else {
                       showEmptyCartView()
                    }

                }

                viewState.newItem?.let {
                    logD(TAG, "New item: ${it.id} ${it.amount} ${it.product.title} ${it.product.description} ${it.product.id}   ")
                }

                viewState.numItemInCache?.let {
                    logD(TAG, "Num in cache: $it")
                    setShoppingCartItemAmountNum(it)
                }

                viewState.myOrderId?.let {
                    logD(TAG, "The order was added? $it")
                    Toast.makeText(context, "Order Placed! $it", Toast.LENGTH_SHORT).show()
                    viewModel.setOrderAdded(null)
                }

            }
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer{ stateMessage ->
            // Shown messages on screen
            stateMessage?.response?.let { response ->
                when(response.message){
                    /*SHOPPING_CART_INSERT_SUCCESS -> {
                        viewModel.clearStateMessage()
                    }*/
                    SHOPPING_CART_GET_ALL_FAILED -> {
                        showEmptyCartView()
                    }

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
                            /*UPDATE_NOTE_FAILED_PK -> {
                                findNavController().popBackStack()
                            }

                            NOTE_DETAIL_ERROR_RETRIEVEING_SELECTED_NOTE -> {
                                findNavController().popBackStack()
                            }*/

                            else -> {/* do nothing*/ }
                        }
                    }
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer{ showLoadingView ->
            // React to loading screens request
            uiController.displayProgressBar(showLoadingView)
        })
    }

    private fun showEmptyCartView(){
        cart_empty_view.visibility = View.VISIBLE
    }

    private fun hideEmptyCartView(){
        cart_empty_view.visibility = View.GONE
    }

    var deliveryPrice = 0

    private fun calculatePaymentInformation() {
        val view = viewModel.getCurrentViewStateOrNew()

        var totalToPay = 0f

        view.itemsList?.let { list ->
            if(list.isNotEmpty()){
                for(item in list){
                    totalToPay += item.product.price * item.amount + deliveryPrice
                }
            }
        }

        cart_total_num.text = "$totalToPay $CURRENCY"
    }

    private fun setShoppingCartItemAmountNum(shoppingCartItemAmountNum : Int){
        cart_item_amount.text =
                String.format(resources.getString(R.string.shopping_cart_amount_text), shoppingCartItemAmountNum.toString())

    }

    private fun navigateToAuth(){
        findNavController().navigate(R.id.action_shoppingCartFragment_to_authFragment)
    }

    //region override ShoppingCartAdapter.Interaction
    override fun onItemClicked(itemId : String) {
        logD(TAG, "onItemClicked")
        viewModel.deleteItem(itemId)
    }

    override fun onDeleteItem(productId: String) {
        logD(TAG, "onDeleteItem")

        // todo we should clear the stack of messages in the close button of the dialogMessage view
        viewModel.clearAllStateMessages() // if we are showing a message on screen, close it

        viewModel.deleteItem(productId)
    }

    override fun onIncrementAmount(productInCart: ShoppingCart) {
        logD(TAG, "onIncrementAmount")

        // todo we should clear the stack of messages in the close button of the dialogMessage view
        viewModel.clearAllStateMessages() // if we are showing a message on screen, close it
        viewModel.incrementItemAmount(productInCart)
    }

    override fun onDecrementAmount(productInCart: ShoppingCart) {
        logD(TAG, "onDecrementAmount")

        // todo we should clear the stack of messages in the close button of the dialogMessage view
        viewModel.clearAllStateMessages() // if we are showing a message on screen, close it

        viewModel.decrementItemAmount(productInCart)
    }

    //endregion override ShoppingCartAdapter.Interaction

}