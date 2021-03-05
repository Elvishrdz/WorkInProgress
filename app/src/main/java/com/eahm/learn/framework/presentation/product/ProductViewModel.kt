package com.eahm.learn.framework.presentation.product

import android.widget.Toast
import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.state.DataState
import com.eahm.learn.business.domain.state.StateEvent
import com.eahm.learn.business.interactors.product.ProductInteractors
import com.eahm.learn.framework.presentation.common.BaseViewModel
import com.eahm.learn.framework.presentation.common.manager.SessionManager
import com.eahm.learn.framework.presentation.product.state.ProductStateEvent.*
import com.eahm.learn.framework.presentation.product.state.ProductViewState
import com.eahm.learn.utils.logD
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class ProductViewModel
@Inject
constructor(
    val sessionManager : SessionManager,
    private val productInteractors : ProductInteractors,
    private val productFactory : ProductFactory
): BaseViewModel<ProductViewState>() {

    private val TAG = "ProductViewModel"

    override fun initNewViewState(): ProductViewState = ProductViewState()

    override fun handleNewData(data: ProductViewState) {
        data.lastPublishedProduct?.let {
            setCurrentProduct(it)
        }
    }

    private fun setCurrentProduct(publishedProduct: Product) {
        val update = getCurrentViewStateOrNew()
        update.lastPublishedProduct = publishedProduct
        setViewState(update)
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job : Flow<DataState<ProductViewState>?> = when(stateEvent) {

            is InsertNewProduct -> {
                // todo fix this
                productInteractors.insertNewProduct.insertNewProduct2(
                    newProduct =  stateEvent.newProduct,
                    stateEvent = stateEvent
                )
            }

            else -> emitInvalidStateEvent(stateEvent)
        }

        launchJob(stateEvent, job)
    }

    fun insertNewProduct() {
        if(sessionManager.isActiveSession){
            val providerId = sessionManager.cachedUser.value?.providerId ?: ""

            if(providerId.isEmpty()) {
                //todo emit message with explanation
                logD(TAG, "You are not a provider yet")
                return
            }

            val newProduct = getCurrentViewStateOrNew()

            newProduct.currentProduct?.let {
                logD(TAG, "Posting from $providerId")
                setStateEvent(
                    InsertNewProduct(
                        newProduct = productFactory.createProduct(
                                it,
                                Provider(
                                      providerId, null, null
                                )
                        )

                    )
                )
            }
        }
        else {
            //todo emit message with explanation
            logD(TAG, "You are not logged in")
        }
    }

    fun setCurrentNewProduct(
        title : String,
        description : String,
        price : Float
    ) {
        if(sessionManager.isActiveSession){
            val update = getCurrentViewStateOrNew()

            // todo use a proper test object
            update.currentProduct = productFactory.createLocalTestProduct(
                    title,
                    description,
                    price
            )

            setViewState(update)
        }
    }
}