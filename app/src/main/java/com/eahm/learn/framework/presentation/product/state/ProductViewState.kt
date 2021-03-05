package com.eahm.learn.framework.presentation.product.state

import android.os.Parcelable
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.state.ViewState
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductViewState(
    var currentProduct : Product? = null, // Current product in the fields
    var lastPublishedProduct : Product? = null
) : Parcelable, ViewState