package com.eahm.learn.business.interactors.shoppingcart

import com.eahm.learn.business.interactors.common.GetShoppingCartItemAmountNum
import com.eahm.learn.business.interactors.common.InsertProductInCart
import com.eahm.learn.business.interactors.shoppingcart.order.AddNewOrder
import com.eahm.learn.framework.presentation.shoppingcart.state.ShoppingCartViewState

class ShoppingCartInteractors (
        val deleteItem : DeleteItem,
        val updateItemAmount: UpdateItemAmount,
        val getAllItems: GetAllItems,
        val getShoppingCartItemAmountNum : GetShoppingCartItemAmountNum<ShoppingCartViewState>,

        val addNewOrder : AddNewOrder

)