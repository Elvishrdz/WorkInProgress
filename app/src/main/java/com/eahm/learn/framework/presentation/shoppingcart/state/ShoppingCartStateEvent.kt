package com.eahm.learn.framework.presentation.shoppingcart.state

import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.business.domain.model.Order
import com.eahm.learn.business.domain.state.StateEvent
import com.eahm.learn.business.domain.state.StateMessage

sealed class ShoppingCartStateEvent : StateEvent {

    class DeleteItem (val itemId: String) : ShoppingCartStateEvent(){
        override fun errorInfo(): String = "Failed to remove item from the cart"
        override fun eventName(): String = "DeleteItem"
        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class UpdateItemAmount(
        val shoppingCartToUpdate : ShoppingCart,
        val newAmount : Int
    ): ShoppingCartStateEvent(){
        override fun errorInfo(): String = "Failed to update item amount"
        override fun eventName(): String = "UpdateItemAmount"
        override fun shouldDisplayProgressBar(): Boolean = true
    }

    class GetAllItems: ShoppingCartStateEvent(){
        override fun errorInfo(): String = "Failed to get all items"
        override fun eventName(): String = "GetAllItems"
        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class GetShoppingCartItemAmountNum: ShoppingCartStateEvent(){
        override fun errorInfo(): String = "Failed to obtaint the total amount of items in the cart"
        override fun eventName(): String = "GetShoppingCartItemAmountNum"
        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class CreateStateMessageEvent(val stateMessage : StateMessage) : ShoppingCartStateEvent() {
        override fun errorInfo(): String = "Error creating a new state message"
        override fun eventName(): String = "CreateStateMessageEvent"
        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class AddNewOrder(
        val newOrder : Order
    ) : ShoppingCartStateEvent(){
        override fun errorInfo(): String = "Error creating the order!"
        override fun eventName(): String = "AddNewOrder"
        override fun shouldDisplayProgressBar(): Boolean = true
    }
}