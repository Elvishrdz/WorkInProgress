package com.eahm.learn.framework.presentation.productlist.state

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.state.StateEvent
import com.eahm.learn.business.domain.state.StateMessage


sealed class ProductListStateEvent: StateEvent {

    class InsertNewProductEvent(
        val title: String
    ): ProductListStateEvent() {

        override fun errorInfo(): String {
            return "Error inserting new product."
        }

        override fun eventName(): String {
            return "InsertNewProductEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    // for testing
    /*class InsertMultipleProductsEvent  : ProductListStateEvent() {

        override fun errorInfo(): String {
            return "Error inserting the products."
        }

        override fun eventName(): String {
            return "InsertMultipleNotesEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }*/

    class DeleteProductEvent(
        val product: Product
    ): ProductListStateEvent(){

        override fun errorInfo(): String {
            return "Error deleting product."
        }

        override fun eventName(): String {
            return "DeleteNoteEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class DeleteMultipleProductsEvent(
        val products: List<Product>
    ): ProductListStateEvent(){

        override fun errorInfo(): String {
            return "Error deleting the selected products."
        }

        override fun eventName(): String {
            return "DeleteMultipleNotesEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class RestoreDeletedProductEvent(
        val product: Product
    ): ProductListStateEvent() {

        override fun errorInfo(): String {
            return "Error restoring the product that was deleted."
        }

        override fun eventName(): String {
            return "RestoreDeletedNoteEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class SearchProductsEvent(
        val clearLayoutManagerState: Boolean = true
    ): ProductListStateEvent(){

        override fun errorInfo(): String {
            return "Error getting list of products."
        }

        override fun eventName(): String {
            return "SearchNotesEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class GetNumProductsInCacheEvent: ProductListStateEvent(){

        override fun errorInfo(): String {
            return "Error getting the number of products from the cache."
        }

        override fun eventName(): String {
            return "GetNumProductInCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class GetShoppingCartNumAmount() : ProductListStateEvent(){
        override fun errorInfo(): String = "Cant retreive the shopping cart element amount"

        override fun eventName(): String = "GetShoppingCartNumAmount"

        override fun shouldDisplayProgressBar(): Boolean = false

    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): ProductListStateEvent(){

        override fun errorInfo(): String {
            return "Error creating a new state message."
        }

        override fun eventName(): String {
            return "CreateStateMessageEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

}

