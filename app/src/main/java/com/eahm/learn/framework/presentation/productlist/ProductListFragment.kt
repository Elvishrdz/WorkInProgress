package com.eahm.learn.framework.presentation.productlist

import com.eahm.learn.framework.presentation.common.decoration.TopSpacingItemDecoration
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.domain.util.DateUtil
import com.eahm.learn.business.domain.util.TodoCallback
import com.eahm.learn.R
import com.eahm.learn.business.interactors.common.DeleteProduct.Companion.DELETE_PRODUCT_SUCCESS
import com.eahm.learn.framework.presentation.common.BaseFragment
import com.eahm.learn.framework.presentation.common.extensions.hideKeyboard
import com.eahm.learn.framework.presentation.productdetail.PRODUCT_DETAIL_SELECTED_PRODUCT_BUNDLE_KEY
import com.eahm.learn.framework.presentation.productlist.adapter.ProductListAdapter
import com.eahm.learn.framework.presentation.productlist.helper.ItemTouchHelperAdapter
import com.eahm.learn.framework.presentation.productlist.helper.ProductItemTouchHelperCallback
import com.eahm.learn.framework.presentation.productlist.state.ProductListToolbarState.*
import com.eahm.learn.framework.presentation.productlist.state.ProductListViewState
import com.eahm.learn.utils.logD
import kotlinx.android.synthetic.main.fragment_product_list.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

const val PRODUCT_LIST_STATE_BUNDLE_KEY = "com.eahm.learn.framework.presentation.productlist.state"

@FlowPreview
@ExperimentalCoroutinesApi
class ProductListFragment
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dateUtil: DateUtil
): BaseFragment(R.layout.fragment_product_list),
    ProductListAdapter.Interaction,
    ItemTouchHelperAdapter
{

    //region variables
    private val TAG = "ProductListFragment"

    val viewModel: ProductListViewModel by viewModels {
        viewModelFactory
    }

    private var listAdapter: ProductListAdapter? = null
    private var itemTouchHelper: ItemTouchHelper? = null
    //endregion variables

    override fun inject() {
        getAppComponent().inject(this)
    }

    //region fragment overrides
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()

    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupRecyclerView()
        setupSwipeRefresh()
        setupFAB()
        subscribeObservers()

        restoreInstanceState(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        // todo we should clear the stack of messages in the close button of the dialogMessage view
        viewModel.clearAllStateMessages() // if we are showing a message on screen, close it

        viewModel.retrieveNumProductsInCache()
        //viewModel.clearList()
        viewModel.refreshSearchQuery() // Obtain first products
        viewModel.getShoppingCartItemAmountNum()   // Last event because it cancel all previous jobs to execute
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value

        //TODO save just a limited amount of products. if exceed then clean
        // if(current list is more than X amount) viewState?.productList =  ArrayList()

        outState.putParcelable(PRODUCT_LIST_STATE_BUNDLE_KEY, viewState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listAdapter = null // can leak memory
        itemTouchHelper = null // can leak memory
    }

    //endregion fragment overrides

    //region setups
    private fun setupUI(){
        view?.hideKeyboard()

        shopping_cart.setOnClickListener{
            navigateToShoppingCart()
        }

        user.setOnClickListener{
            if(viewModel.isActiveSession){
                navigateToUserProfile()
            }
            else {
                navigateToAuth()
            }
        }
        product.setOnClickListener{
            navigateToProviderProductList()
        }
        btn_orderProviders.setOnClickListener{
            navigateToProviderOrders()
        }
        btn_orders.setOnClickListener{
            navigateToOrderFragment()
        }
    }

    private fun setupRecyclerView(){
        recycler_view.apply {
            //layoutManager = LinearLayoutManager(activity)
            layoutManager =  GridLayoutManager(activity, 3)

            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)

            itemTouchHelper = ItemTouchHelper(
                ProductItemTouchHelperCallback(
                    this@ProductListFragment,
                    viewModel.productListInteractionManager
                )
            )

            listAdapter = ProductListAdapter(
                this@ProductListFragment,
                viewLifecycleOwner,
                //todo check this option viewModel.productListInteractionManager.selectedProducts
                viewModel.productListInteractionManager.currentProducts,
                dateUtil
            )

            itemTouchHelper?.attachToRecyclerView(this)
            // TODO SET UP PAGINATION
            /*addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == listAdapter?.itemCount?.minus(1)) {
                        viewModel.nextPage()
                    }
                }
            })*/
            adapter = listAdapter
        }
    }

    private fun setupSearchView(){

        /* val searchViewToolbar: Toolbar? = toolbar_content_container
             .findViewById<Toolbar>(R.id.searchview_toolbar)

         searchViewToolbar?.let { toolbar ->

             val searchView = toolbar.findViewById<SearchView>(R.id.search_view)

             val searchPlate: SearchView.SearchAutoComplete?
                     = searchView.findViewById(androidx.appcompat.R.id.search_src_text)

             // can't use QueryTextListener in production b/c can't submit an empty string
             when{
                 *//*androidTestUtils.isTest() -> {
                    searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            viewModel.setQuery(query)
                            startNewSearch()
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            return true
                        }

                    })
                }*//*

                else ->{
                    searchPlate?.setOnEditorActionListener { v, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                            || actionId == EditorInfo.IME_ACTION_SEARCH ) {
                            val searchQuery = v.text.toString()
                            viewModel.setQuery(searchQuery)
                            startNewSearch()
                        }
                        true
                    }
                }
            }
        }*/
    }

    private fun setupFAB(){
        /* add_new_product_fab.setOnClickListener {
             uiController.displayInputCaptureDialog(
                 getString(com.codingwithmitch.cleanproducts.R.string.text_enter_a_title),
                 object: DialogInputCaptureCallback{
                     override fun onTextCaptured(text: String) {
                         val newProduct = viewModel.createNewProduct(title = text)
                         viewModel.setStateEvent(
                             InsertNewProductEvent(
                                 title = newProduct.title
                             )
                         )
                     }
                 }
             )
         }*/
    }

    private fun setupSwipeRefresh(){
        swipe_refresh.setOnRefreshListener {
            startNewSearch()
            swipe_refresh.isRefreshing = false
        }
    }

    private fun setupFilterButton(){
        /*val searchViewToolbar: Toolbar? = toolbar_content_container
            .findViewById<Toolbar>(R.id.searchview_toolbar)
        searchViewToolbar?.findViewById<ImageView>(R.id.action_filter)?.setOnClickListener {
            showFilterDialog()
        }*/
    }

    //endregion setups

    //region navigation
    private fun navigateToDetailFragment(selectedProduct: Product){
        val bundle = bundleOf(PRODUCT_DETAIL_SELECTED_PRODUCT_BUNDLE_KEY to selectedProduct)
        findNavController().navigate(
            R.id.action_productListFragment_to_productDetailFragment,
            bundle
        )
        viewModel.setProduct(null)
    }

    private fun navigateToShoppingCart(){
        findNavController().navigate(R.id.action_productListFragment_to_shoppingCartFragment)
    }

    private fun navigateToAuth(){
        findNavController().navigate(R.id.action_productListFragment_to_authFragment)
    }

    private fun navigateToUserProfile(){
        findNavController().navigate(R.id.action_productListFragment_to_profileFragment)
    }

    private fun navigateToProviderProductList() {
        findNavController().navigate(R.id.action_productListFragment_to_userProductListFragment)
    }

    private fun navigateToProviderOrders() {
        findNavController().navigate(R.id.action_productListFragment_to_orderListProviderFragment)
    }

    private fun navigateToOrderFragment(){
        findNavController().navigate(R.id.action_productListFragment_to_orderListFragment)
    }
    //endregion navigation

    //region overrides ItemTouchHelperAdapter
    override fun onItemSwiped(position: Int) {
        if(!viewModel.isDeletePending()){
            listAdapter?.getProduct(position)?.let { product ->
                viewModel.beginPendingDelete(product)
            }
        }
        else{
            listAdapter?.notifyDataSetChanged()
        }
    }
    //endregion ItemTouchHelperAdapter

    //region overrides ProductListAdapter.Interaction

    override fun onItemSelected(position: Int, item: Product) {
        if(isMultiSelectionModeEnabled()){
            viewModel.addOrRemoveProductFromSelectedList(item)
        }
        else{
            viewModel.setProduct(item)
            Toast.makeText(context, "$position ${listAdapter?.getProduct(position)?.photos?.get(0)?.photoUrl}", Toast.LENGTH_LONG).show()
        }
    }

    override fun restoreListPosition() {
        viewModel.getLayoutManagerState()?.let { lmState ->
            recycler_view?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun isMultiSelectionModeEnabled()  = viewModel.isMultiSelectionStateActive()

    override fun activateMultiSelectionMode() = viewModel.setToolbarState(MultiSelectionState())

    override fun isProductSelected(product: Product): Boolean {
        return viewModel.isProductSelected(product)
    }

    //endregion overrides ProductListAdapter.Interaction

    private fun subscribeObservers(){

        viewModel.user.observe(viewLifecycleOwner, Observer {
            logD(TAG, "user status: ${viewModel.isActiveSession} - $it")
            if(viewModel.isActiveSession && it != null){
                product.visibility = View.VISIBLE
                if(it.providerId.isNotEmpty()) btn_orderProviders.visibility = View.VISIBLE else btn_orderProviders.visibility = View.GONE
                btn_orders.visibility = View.VISIBLE
            }
            else {
                product.visibility = View.GONE
                btn_orderProviders.visibility = View.GONE
                btn_orders.visibility = View.GONE
            }
        })

        viewModel.toolbarState.observe(viewLifecycleOwner, Observer{ toolbarState ->

            /*when(toolbarState){

                is MultiSelectionState -> {
                    enableMultiSelectToolbarState()
                    disableSearchViewToolbarState()
                }

                is SearchViewState -> {
                    enableSearchViewToolbarState()
                    disableMultiSelectToolbarState()
                }
            }*/
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer{ viewState ->
            if(viewState != null){
                viewState.productList?.let {productList ->
                    /*if(viewModel.isPaginationExhausted() && !viewModel.isQueryExhausted()){
                        viewModel.setQueryExhausted(true)
                    }*/
                    if(productList.isNotEmpty()){
                        viewModel.setCurrentProductList(productList)
                    }

                    listAdapter?.submitList(productList)
                    //listAdapter?.notifyDataSetChanged()
                }

                // a product been inserted or selected
                viewState.newProduct?.let { newProduct ->
                    navigateToDetailFragment(newProduct)
                }

                viewState.numItemsInCart?.let { numItemsInCart ->
                    setShoppingCartNumAmount(numItemsInCart)
                }

            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer {
            printActiveJobs()
            uiController.displayProgressBar(it)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let { message ->
                if(message.response.message?.equals(DELETE_PRODUCT_SUCCESS) == true){
                    showUndoSnackbarDeleteProduct()
                }
                else{
                    uiController.onResponseReceived(
                        response = message.response,
                        stateMessageCallback = object: StateMessageCallback {
                            override fun removeMessageFromStack() {
                                viewModel.clearStateMessage()
                            }
                        }
                    )
                }
            }
        })
    }

    private fun showUndoSnackbarDeleteProduct(){
        uiController.onResponseReceived(
            response = Response(
                message = /*DELETE_PRODUCT_PENDING*/"",
                uiComponentType = UIComponentType.SnackBar(
                    undoCallback = object : SnackbarUndoCallback {
                        override fun undo() {
                            viewModel.undoDelete()
                        }
                    },
                    onDismissCallback = object: TodoCallback {
                        override fun execute() {
                            // if the product is not restored, clear pending product
                            viewModel.setProductPendingDelete(null)
                        }
                    }
                ),
                messageType = MessageType.Info()
            ),
            stateMessageCallback = object: StateMessageCallback{
                override fun removeMessageFromStack() {
                    viewModel.clearStateMessage()
                }
            }
        )
    }

    // for debugging
    private fun printActiveJobs(){

        for((index, job) in viewModel.getActiveJobs().withIndex()){
            //printLogD("ProductList", "${index}: ${job}")
        }
    }

    private fun clearArgs(){
        arguments?.clear()
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?){
        savedInstanceState?.let { inState ->
            (inState[PRODUCT_LIST_STATE_BUNDLE_KEY] as ProductListViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    private fun saveLayoutManagerState(){
        recycler_view.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    private fun deleteProducts(){
        /*viewModel.setStateEvent(
            CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = DELETE_PRODUCTS_ARE_YOU_SURE,
                        uiComponentType = UIComponentType.AreYouSureDialog(
                            object : AreYouSureCallback {
                                override fun proceed() {
                                    viewModel.deleteProducts()
                                }

                                override fun cancel() {
                                    // do nothing
                                }
                            }
                        ),
                        messageType = MessageType.Info()
                    )
                )
            )
        )*/
    }

    private fun startNewSearch(){
        viewModel.clearList()
        viewModel.loadFirstPage()
    }

    fun showFilterDialog(){

       /* activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.layout_filter)

            val view = dialog.getCustomView()

            val filter = viewModel.getFilter()
            val order = viewModel.getOrder()

            view.findViewById<RadioGroup>(R.id.filter_group).apply {
                when (filter) {
                    PRODUCT_FILTER_DATE_CREATED -> check(R.id.filter_date)
                    PRODUCT_FILTER_TITLE -> check(R.id.filter_title)
                }
            }

            view.findViewById<RadioGroup>(R.id.order_group).apply {
                when (order) {
                    PRODUCT_ORDER_ASC -> check(R.id.filter_asc)
                    PRODUCT_ORDER_DESC -> check(R.id.filter_desc)
                }
            }

            view.findViewById<TextView>(R.id.positive_button).setOnClickListener {

                val newFilter =
                    when (view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId) {
                        R.id.filter_title -> PRODUCT_FILTER_TITLE
                        R.id.filter_date -> PRODUCT_FILTER_DATE_CREATED
                        else -> PRODUCT_FILTER_DATE_CREATED
                    }

                val newOrder =
                    when (view.findViewById<RadioGroup>(R.id.order_group).checkedRadioButtonId) {
                        R.id.filter_desc -> "-"
                        else -> ""
                    }

                viewModel.apply {
                    saveFilterOptions(newFilter, newOrder)
                    setProductFilter(newFilter)
                    setProductOrder(newOrder)
                }

                startNewSearch()

                dialog.dismiss()
            }

            view.findViewById<TextView>(R.id.negative_button).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }*/
    }

    // todo enable multi selection
    /* private fun enableMultiSelectToolbarState(){
         view?.let { v ->
             val view = View.inflate(
                 v.context,
                 R.layout.layout_multiselection_toolbar,
                 null
             )
             view.layoutParams = LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT,
                 LinearLayout.LayoutParams.MATCH_PARENT
             )
             toolbar_content_container.addView(view)
             setupMultiSelectionToolbar(view)
         }
     }

     private fun setupMultiSelectionToolbar(parentView: View){
         parentView
             .findViewById<ImageView>(R.id.action_exit_multiselect_state)
             .setOnClickListener {
                 viewModel.setToolbarState(SearchViewState())
             }

         parentView
             .findViewById<ImageView>(R.id.action_delete_products)
             .setOnClickListener {
                 deleteProducts()
             }
     }

     private fun enableSearchViewToolbarState(){
         view?.let { v ->
             val view = View.inflate(
                 v.context,
                 R.layout.layout_searchview_toolbar,
                 null
             )
             view.layoutParams = LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT,
                 LinearLayout.LayoutParams.MATCH_PARENT
             )
             toolbar_content_container.addView(view)
             setupSearchView()
             setupFilterButton()
         }
     }

     private fun disableMultiSelectToolbarState(){
         view?.let {
             val view = toolbar_content_container
                 .findViewById<Toolbar>(R.id.multiselect_toolbar)
             toolbar_content_container.removeView(view)
             viewModel.clearSelectedProducts()
         }
     }

     private fun disableSearchViewToolbarState(){
         view?.let {
             val view = toolbar_content_container
                 .findViewById<Toolbar>(R.id.searchview_toolbar)
             toolbar_content_container.removeView(view)
         }
     }

 */

    //region setters
    private fun setShoppingCartNumAmount(numItemsInCart: Int) {
        shopping_cart.text = numItemsInCart.toString()
    }
    //endregion setters



}


















































