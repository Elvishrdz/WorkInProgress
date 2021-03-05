package com.eahm.learn.framework.presentation.productlist

import android.content.SharedPreferences
import android.os.Parcelable
import androidx.lifecycle.LiveData
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.business.domain.state.*
import com.eahm.learn.business.interactors.productlist.DeleteMultipleProducts.Companion.DELETE_PRODUCTS_YOU_MUST_SELECT
import com.eahm.learn.business.interactors.productlist.ProductListInteractors
import com.eahm.learn.business.interactors.shoppingcart.ShoppingCartInteractors
import com.eahm.learn.framework.datasource.cache.dao.PRODUCT_FILTER_DATE_CREATED
import com.eahm.learn.framework.datasource.cache.dao.PRODUCT_ORDER_DESC
import com.eahm.learn.framework.datasource.preferences.PreferenceKeys.Companion.PRODUCT_FILTER
import com.eahm.learn.framework.datasource.preferences.PreferenceKeys.Companion.PRODUCT_ORDER
import com.eahm.learn.framework.presentation.common.BaseViewModel
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import com.eahm.learn.framework.presentation.productlist.state.ProductListInteractionManager
import com.eahm.learn.framework.presentation.productlist.state.ProductListStateEvent.*
import com.eahm.learn.framework.presentation.productlist.state.ProductListToolbarState
import com.eahm.learn.framework.presentation.productlist.state.ProductListViewState
import com.eahm.learn.framework.presentation.productlist.state.ProductListViewState.*
import com.eahm.learn.utils.logD
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

const val DELETE_PENDING_ERROR = "There is already a pending delete operation."
const val PRODUCT_PENDING_DELETE_BUNDLE_KEY = "pending_delete"

@ExperimentalCoroutinesApi
@FlowPreview
@Singleton
class ProductListViewModel
@Inject
constructor(
        private val sessionManager: SessionManager,
        private val productInteractors: ProductListInteractors,
        private val shoppingCartInteractors: ShoppingCartInteractors,
        private val productFactory: ProductFactory,
        private val editor: SharedPreferences.Editor,
        sharedPreferences: SharedPreferences
): BaseViewModel<ProductListViewState>(){

    //region variables
    val productListInteractionManager = ProductListInteractionManager()

    val toolbarState: LiveData<ProductListToolbarState>
        get() = productListInteractionManager.toolbarState
    //endregion variables

    //region session access
    val user : LiveData<User?>
        get() = sessionManager.cachedUser

    val isActiveSession : Boolean
        get() = sessionManager.isActiveSession

    fun logout() = sessionManager.logout()
    //endregion session access

    init {
        setProductFilter(
            sharedPreferences.getString(
                PRODUCT_FILTER,
                PRODUCT_FILTER_DATE_CREATED
            )
        )
        setProductOrder(
            sharedPreferences.getString(
                PRODUCT_ORDER,
                PRODUCT_ORDER_DESC
            )
        )
    }

    //region overrides BaseViewModel
    override fun initNewViewState(): ProductListViewState {
        return ProductListViewState()
    }

    override fun handleNewData(data: ProductListViewState) {
        data.let { viewState ->
            viewState.productList?.let { productList ->
                setProductListData(productList)
            }

            viewState.numProductsInCache?.let { numProducts ->
                setNumProductsInCache(numProducts)
            }

            viewState.newProduct?.let { product ->
                setProduct(product)
            }

            viewState.productPendingDelete?.let { restoredProduct ->
                restoredProduct.product?.let { product ->
                    setRestoredProductId(product)
                }
                setProductPendingDelete(null)
            }

            viewState.numItemsInCart?.let { numItemsInCart ->
                logD("CART_CHECK", "SET CART IN VIEW STATE")
                setShoppingCartItemAmountNum(numItemsInCart)
            }
        }

    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<ProductListViewState>?>
        = when(stateEvent){

            is InsertNewProductEvent -> {
                productInteractors.insertNewProduct.insertNewProduct(
                        title = stateEvent.title,
                        stateEvent = stateEvent
                )
            }

            is DeleteProductEvent -> {
                productInteractors.deleteProduct.deleteProduct(
                        product = stateEvent.product,
                        stateEvent = stateEvent
                )
            }

            is DeleteMultipleProductsEvent -> {
                productInteractors.deleteMultipleProducts.deleteProducts(
                        products = stateEvent.products,
                        stateEvent = stateEvent
                )
            }

            is SearchProductsEvent -> {
                if(stateEvent.clearLayoutManagerState){
                    clearLayoutManagerState()
                }

                productInteractors.searchProducts.searchProducts(
                        query =  getSearchQuery(),
                        filter = getFilter(),
                        order = getOrder(),
                        page = getPage(),
                        stateEvent = stateEvent
                )
            }

            is GetNumProductsInCacheEvent -> {
                productInteractors.getNumProducts.getNumProducts(
                        stateEvent = stateEvent
                )
            }

            is GetShoppingCartNumAmount -> {
                // Remove any existing stateEvent to be able to execute again the event.
                removeStateEvent(stateEvent)

                productInteractors.getShoppingCartItemAmountNum.getShoppingCartItemAmountNum(
                    isLocal = !sessionManager.isActiveSession,
                    stateEvent = stateEvent
                )
            }

            is CreateStateMessageEvent -> {
                emitStateMessageEvent(
                        stateMessage = stateEvent.stateMessage,
                        stateEvent = stateEvent
                )
            }

            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }

        launchJob(stateEvent, job)
    }
    //endregion overrides BaseViewModel

    //region getters
    fun getSelectedProducts() = productListInteractionManager.getSelectedProducts()

    fun getFilter(): String {
        return getCurrentViewStateOrNew().filter
            ?: PRODUCT_FILTER_DATE_CREATED
    }

    fun getOrder(): String {
        return getCurrentViewStateOrNew().order
            ?: PRODUCT_ORDER_DESC
    }

    fun getSearchQuery(): String {
        return getCurrentViewStateOrNew().searchQuery ?: return ""
    }

    private fun getPage(): Int{
        return getCurrentViewStateOrNew().page
            ?: return 1
    }

    fun getProductListSize() = getCurrentViewStateOrNew().productList?.size?: 0

    private fun getNumProductsInCache() = getCurrentViewStateOrNew().numProductsInCache?: 0

    fun getLayoutManagerState(): Parcelable? {
        return getCurrentViewStateOrNew().layoutManagerState
    }

    // for debugging
    fun getActiveJobs() = dataChannelManager.getActiveJobs()

    //endregion getters

    //region setters

    fun setToolbarState(state: ProductListToolbarState) = productListInteractionManager.setToolbarState(state)

    fun setLayoutManagerState(layoutManagerState: Parcelable){
        val update = getCurrentViewStateOrNew()
        update.layoutManagerState = layoutManagerState
        setViewState(update)
    }

    private fun setProductListData(productsList: ArrayList<Product>){
        val update = getCurrentViewStateOrNew()
        update.productList = productsList
        setViewState(update)
    }

    fun setQueryExhausted(isExhausted: Boolean){
        val update = getCurrentViewStateOrNew()
        update.isQueryExhausted = isExhausted
        setViewState(update)
    }

    // can be selected from Recyclerview or created new from dialog
    fun setProduct(product: Product?){
        val update = getCurrentViewStateOrNew()
        update.newProduct = product
        setViewState(update)
    }

    fun setQuery(query: String?){
        val update =  getCurrentViewStateOrNew()
        update.searchQuery = query
        setViewState(update)
    }

    // if a product is deleted and then restored, the id will be incorrect.
    // So need to reset it here.
    private fun setRestoredProductId(restoredProduct: Product){
        val update = getCurrentViewStateOrNew()
        update.productList?.let { productList ->
            for((index, product) in productList.withIndex()){
                if(product.title.equals(restoredProduct.title)){
                    productList.remove(product)
                    productList.add(index, restoredProduct)
                    update.productList = productList
                    break
                }
            }
        }
        setViewState(update)
    }

    fun setProductPendingDelete(product: Product?){
        val update = getCurrentViewStateOrNew()
        if(product != null){
            update.productPendingDelete = ProductPendingDelete(
                product = product,
                listPosition = findListPositionOfProduct(product)
            )
        }
        else{
            update.productPendingDelete = null
        }
        setViewState(update)
    }

    private fun setNumProductsInCache(numProducts: Int){
        val update = getCurrentViewStateOrNew()
        update.numProductsInCache = numProducts
        setViewState(update)
    }

    fun setProductFilter(filter: String?){
        filter?.let{
            val update = getCurrentViewStateOrNew()
            update.filter = filter
            setViewState(update)
        }
    }

    fun setProductOrder(order: String?){
        val update = getCurrentViewStateOrNew()
        update.order = order
        setViewState(update)
    }

    fun setShoppingCartItemAmountNum(numItemsInCart : Int){
        val update = getCurrentViewStateOrNew()
        update.numItemsInCart = numItemsInCart
        setViewState(update)
    }

    //endregion setters

    //region booleans
    fun isMultiSelectionStateActive() = productListInteractionManager.isMultiSelectionStateActive()

    fun isDeletePending(): Boolean{
        val pendingProduct = getCurrentViewStateOrNew().productPendingDelete
        if(pendingProduct != null){
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                            response = Response(
                                    message = DELETE_PENDING_ERROR,
                                    uiComponentType = UIComponentType.Toast(),
                                    messageType = MessageType.Info()
                            )
                    )
                )
            )
            return true
        }
        else{
            return false
        }
    }

    fun isQueryExhausted(): Boolean{
        //printLogD("ProductListViewModel", "is query exhasuted? ${getCurrentViewStateOrNew().isQueryExhausted?: true}")
        return getCurrentViewStateOrNew().isQueryExhausted?: true
    }

    fun isProductSelected(product: Product): Boolean = productListInteractionManager.isProductSelected(product)

    fun isPaginationExhausted() = getProductListSize() >= getNumProductsInCache()

    //endregion booleans

    private fun findListPositionOfProduct(product: Product?): Int {
        val viewState = getCurrentViewStateOrNew()
        viewState.productList?.let { productList ->
            for((index, item) in productList.withIndex()){
                if(item.id == product?.id){
                    return index
                }
            }
        }
        return 0
    }

    fun deleteProducts(){
        if(getSelectedProducts().size > 0){
            setStateEvent(DeleteMultipleProductsEvent(getSelectedProducts()))
            removeSelectedProductsFromList()
        }
        else{
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = DELETE_PRODUCTS_YOU_MUST_SELECT,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Info()
                        )
                    )
                )
            )
        }
    }

    fun beginPendingDelete(product: Product){
        setProductPendingDelete(product)
        removePendingProductFromList(product)
        setStateEvent(
            DeleteProductEvent(
                product = product
            )
        )
    }

    fun undoDelete(){
        // replace product in viewstate
        val update = getCurrentViewStateOrNew()
        update.productPendingDelete?.let { product ->
            if(product.listPosition != null && product.product != null){
                update.productList?.add(
                    product.listPosition as Int,
                    product.product as Product
                )
                setStateEvent(RestoreDeletedProductEvent(product.product as Product))
            }
        }
        setViewState(update)
    }

    private fun removeSelectedProductsFromList(){
        val update = getCurrentViewStateOrNew()
        update.productList?.removeAll(getSelectedProducts())
        setViewState(update)
        clearSelectedProducts()
    }

    private fun removePendingProductFromList(product: Product?){
        val update = getCurrentViewStateOrNew()
        val list = update.productList
        if(list?.contains(product) == true){
            list.remove(product)
            update.productList = list
            setViewState(update)
        }
    }

    private fun resetPage(){
        val update = getCurrentViewStateOrNew()
        update.page = 1
        setViewState(update)
    }

    fun loadFirstPage() {
        setQueryExhausted(false)
        resetPage()
        setStateEvent(SearchProductsEvent())
        //printLogD("ProductListViewModel", "loadFirstPage: ${getCurrentViewStateOrNew().searchQuery}")
    }

    fun nextPage(){
        if(!isQueryExhausted()){
           // printLogD("ProductListViewModel", "attempting to load next page...")
            clearLayoutManagerState()
            incrementPageNumber()
            setStateEvent(SearchProductsEvent())
        }
    }

    private fun incrementPageNumber(){
        val update = getCurrentViewStateOrNew()
        val page = update.copy().page ?: 1
        update.page = page.plus(1)
        setViewState(update)
    }

    fun retrieveNumProductsInCache(){
        setStateEvent(GetNumProductsInCacheEvent())
    }

    fun getShoppingCartItemAmountNum(){
        setStateEvent(GetShoppingCartNumAmount())
    }

    fun refreshSearchQuery(){
        setQueryExhausted(false)

        // Execute the first search
        setStateEvent(SearchProductsEvent(false))
    }

    fun addOrRemoveProductFromSelectedList(product: Product) = productListInteractionManager.addOrRemoveProductFromSelectedList(product)

    fun setCurrentProductList(currentProducts : ArrayList<Product>) = productListInteractionManager.setCurrentProducts(currentProducts)

    fun saveFilterOptions(filter: String, order: String){
        editor.putString(PRODUCT_FILTER, filter)
        editor.apply()

        editor.putString(PRODUCT_ORDER, order)
        editor.apply()
    }

    fun clearList(){
        val update = getCurrentViewStateOrNew()
        update.productList = ArrayList()
        setViewState(update)
    }

    fun clearLayoutManagerState(){
        val update = getCurrentViewStateOrNew()
        update.layoutManagerState = null
        setViewState(update)
    }

    fun clearSelectedProducts() = productListInteractionManager.clearSelectedProducts()

    // workaround for tests
    // can't submit an empty string to SearchViews
    fun clearSearchQuery(){
        setQuery("")
        clearList()
        loadFirstPage()
    }

    fun insertFakeProducts() {
        setStateEvent(
            InsertNewProductEvent("")
        )
    }

}
