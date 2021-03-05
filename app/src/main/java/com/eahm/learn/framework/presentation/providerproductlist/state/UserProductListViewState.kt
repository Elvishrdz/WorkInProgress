package com.eahm.learn.framework.presentation.providerproductlist.state

import android.os.Parcelable
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.state.ViewState
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserProductListViewState(
    var userProductList : List<Product>? = null
) : Parcelable, ViewState