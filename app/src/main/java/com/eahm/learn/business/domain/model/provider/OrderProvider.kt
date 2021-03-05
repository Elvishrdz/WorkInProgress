package com.eahm.learn.business.domain.model.provider

import android.os.Parcelable
import com.eahm.learn.business.domain.model.Address
import com.eahm.learn.business.domain.model.Product
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderProvider(
    val id : String,
    val address : Address,
    val productId : Product, // reference to the current product
    val productOrdered : Product // a clone of the product when the user did the order. Contains the values at that moment. (product values can change through time)
) : Parcelable