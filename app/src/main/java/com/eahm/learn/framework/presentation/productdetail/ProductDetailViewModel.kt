package com.eahm.learn.framework.presentation.productdetail

import android.widget.Toast
import androidx.lifecycle.LiveData
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.business.domain.state.DataState
import com.eahm.learn.business.domain.state.StateEvent
import com.eahm.learn.business.interactors.common.InsertProductInCart.Companion.SHOPPING_CART_LOCAL
import com.eahm.learn.business.interactors.productdetail.ProductDetailInteractors
import com.eahm.learn.framework.presentation.common.BaseViewModel
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import com.eahm.learn.framework.presentation.productdetail.state.ProductDetailStateEvent.*
import com.eahm.learn.framework.presentation.productdetail.state.ProductDetailViewState
import com.eahm.learn.framework.presentation.productlist.state.ProductListStateEvent.*
import com.eahm.learn.utils.logD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val PRODUCT_DETAIL_ERROR_RETRIEVEING_SELECTED_PRODUCT = "Error retrieving selected product from bundle."
const val PRODUCT_DETAIL_SELECTED_PRODUCT_BUNDLE_KEY = "selectedProduct"
const val PRODUCT_TITLE_CANNOT_BE_EMPTY = "Product title can not be empty."


@ExperimentalCoroutinesApi
@FlowPreview
class ProductDetailViewModel
@Inject
constructor(
    private val productDetailInteractors: ProductDetailInteractors,
    private val sessionManager : SessionManager
): BaseViewModel<ProductDetailViewState>(){

    private val TAG = "productDetailViewModel"

    //region session access
    val user : LiveData<User?>
        get() = sessionManager.cachedUser

    val isActiveSession : Boolean
        get() = sessionManager.isActiveSession

    //endregion session access

    override fun handleNewData(data: ProductDetailViewState) {
        data.let { viewState ->
            logD(TAG, "Handle new data called.")

            viewState.product?.let {

            }

            viewState.productInCart?.let {
                logD(TAG, "Product in cart: ${it.product?.id} | ${it.amount}")
                setProductInCart(it)
            }

            viewState.provider?.let {
                logD(TAG, "We got the provider information ${it.business?.name} ${it.user?.name_first}")
                setProviderInformation(it)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<ProductDetailViewState>?> = when(stateEvent){
            is InsertProductInCart -> {
                productDetailInteractors.insertProductInCart.insertProductInCart(
                    userId = sessionManager.cachedUser.value?.id ?: SHOPPING_CART_LOCAL,
                    product = stateEvent.product,
                    stateEvent = stateEvent
                )
            }

            is SearchProductInCart ->{
                logD(TAG, "search ${stateEvent.shoppingCartId}")
                productDetailInteractors.searchProductInCart.searchProduct(
                    shoppingCartID = stateEvent.shoppingCartId,
                    stateEvent = stateEvent
                )
            }

            is SyncProviderInformation -> {
                logD(TAG, "SyncProviderInformation ${stateEvent.providerId}")
                productDetailInteractors.syncProvider.syncProvider(
                    providerId = stateEvent.providerId,
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


    private fun setProviderInformation(result : Provider){
        val update = getCurrentViewStateOrNew()
        update.provider = result
        setViewState(update)
    }

    override fun initNewViewState(): ProductDetailViewState {
        return ProductDetailViewState()
    }

    //region setters
    fun setProduct(product: Product?) {
        val update = getCurrentViewStateOrNew()
        update.product = product
        setViewState(update)
    }


    fun setProductInCart(productInCart: ShoppingCart?) {
        val update = getCurrentViewStateOrNew()
        update.productInCart = productInCart
        setViewState(update)
    }

    fun addCurrentProductToShoppingCart() {
        logD(TAG, "addCurrentProductToShoppingCart")
        val current = getCurrentViewStateOrNew()

        if(current.productInCart == null){
            current.product?.let {
                setStateEvent(
                    InsertProductInCart(
                        product = it,
                        amount = 1
                    )
                )
            }
        }
    }

    fun checkProductInShoppingCart() {
        val current = getCurrentViewStateOrNew()

        if(sessionManager.isActiveSession){
            val providerId = user.value?.providerId ?: ""

            if(providerId == current.product?.provider?.id){
                logD(TAG, "Cart: This product is yours. $providerId = ${current.product?.provider?.id}")
                // todo emit message and notify the provider
                return
            }
        }

        if(current.productInCart == null){
            current.product?.let {
                setStateEvent(SearchProductInCart(it.id))
            }
        }

    }

    fun getProviderInformation() {
        val current = getCurrentViewStateOrNew()

        if(sessionManager.isActiveSession){
            val providerId = user.value?.providerId ?: ""

            if(providerId == current.product?.provider?.id){
                logD(TAG, "Provider: This product is yours. $providerId = ${current.product?.provider?.id}")
                // todo emit message and notify the provider
                return
            }
        }


        if(current.provider == null){
            current.product?.let {
                setStateEvent(SyncProviderInformation(it.provider.id))
            }
        }

    }

    //endregion setters

}