package com.eahm.learn.framework.presentation.shoppingcart.state

import android.os.Parcelable
import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.business.domain.state.ViewState
import com.eahm.learn.framework.presentation.shoppingcart.Filter
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShoppingCartViewState (
    var itemsList : MutableList<ShoppingCart>? = null,
    var newItem : ShoppingCart? = null, // When added to the shopping cart
    var updatedItem : ShoppingCart? = null, // When we update an item amount in the list
    var numItemInCache : Int? = null, // show the amount of items in the cart
    var filter : Filter? = null,
    var newItems : List<ShoppingCart>? = null, // Testing purposes
    var deletedItemID : String? = null, // return the deleted item ID

    var myOrderId : String? = null // Return the OrderID if was successfully placed
) : Parcelable, ViewState