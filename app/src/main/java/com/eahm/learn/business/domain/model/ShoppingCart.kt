package com.eahm.learn.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShoppingCart(
    val id : String,
    val product: Product,
    val amount : Int
) : Parcelable