package com.eahm.learn.framework.presentation.productdetail.state

import android.os.Parcelable
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.model.Provider
import com.eahm.learn.business.domain.model.ShoppingCart
import com.eahm.learn.business.domain.state.ViewState
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetailViewState(
    var product: Product? = null, // Current product details
    var productInCart : ShoppingCart? = null, // Product founded when searching in cart
    var provider : Provider? = null  // Information of the provider of the product
) : Parcelable, ViewState