package com.eahm.learn.framework.presentation.productdetail.state

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.model.Business
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.business.domain.state.StateEvent
import com.eahm.learn.business.interactors.shoppingcart.SearchProductInCart.Companion.SHOPPING_CART_SEARCH_NOT_FOUNDED

sealed class ProductDetailStateEvent : StateEvent {

    class DeleteProduct(stateEvent : StateEvent) : ProductDetailStateEvent(){
        override fun errorInfo(): String = "Fail deleting the product"
        override fun eventName(): String = "DeleteProduct"
        override fun shouldDisplayProgressBar(): Boolean  = true
    }

    class InsertProductInCart (
        val product : Product,
        val amount : Int
    ) : ProductDetailStateEvent(){
        override fun errorInfo(): String = "The product fail adding to the shopping cart"
        override fun eventName(): String = "InsertProductInCart"
        override fun shouldDisplayProgressBar(): Boolean  = true
    }

    class SearchProductInCart(
            val shoppingCartId : String
    ) : ProductDetailStateEvent(){
        override fun errorInfo(): String = SHOPPING_CART_SEARCH_NOT_FOUNDED
        override fun eventName(): String = "SearchProductInCart"
        override fun shouldDisplayProgressBar(): Boolean = false
    }


    class InsertProvider(val newProvider: Provider) : ProductDetailStateEvent(){
        override fun errorInfo(): String = ""
        override fun eventName(): String = ""
        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class SyncProviderInformation(val providerId : String) : ProductDetailStateEvent(){
        override fun errorInfo(): String = "Syncing provider information has failed!"
        override fun eventName(): String = "SyncProviderInformation"
        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class InsertBusiness(val newBusiness : Business) : ProductDetailStateEvent(){
        override fun errorInfo(): String = ""
        override fun eventName(): String = ""
        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class GetBusiness(val businessId : String) : ProductDetailStateEvent(){
        override fun errorInfo(): String = ""
        override fun eventName(): String = ""
        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class InsertUser(val newUser : User) : ProductDetailStateEvent(){
        override fun errorInfo(): String = ""
        override fun eventName(): String = ""
        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class GetUser(val userId : String) : ProductDetailStateEvent(){
        override fun errorInfo(): String = ""
        override fun eventName(): String = ""
        override fun shouldDisplayProgressBar(): Boolean = false
    }

}