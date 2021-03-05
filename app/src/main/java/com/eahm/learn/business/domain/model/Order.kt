package com.eahm.learn.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
        var id : String,
        var delivery : Delivery,
        var order_date : String,
        var products : List<OrderProductList>,
        var clientId : String, // Which account did the order
        var orderOrigin : String, // Device. (phone, laptop, etc)
        var status : String
): Parcelable

@Parcelize
data class Delivery(
    val address : String,
    val city : String,
    val country : String,
    val description : String,
    val postCode : String,
    val town : String
) : Parcelable

@Parcelize
data class OrderProductList(
    val amount : Int,
    val productId : String, // Reference to the product
    var orderId : String = "" // Reference to the orderId where the productId belongs
): Parcelable