package com.eahm.learn.business.domain.model.pending

import android.os.Parcelable
import com.eahm.learn.business.domain.model.Address
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Purchase(
        val id : String,
        val purchasedProducts : List<PurchasedProducts>,
        val taxPercentageAmount : String,
        val deliverCost : String,
        val totalPayed : String,
        val description : String,
        val device : String,
        val date : String,
        val clientID : String,
        val clientFullName: String,
        val clientDeliveryAddress : Address,
        val clientDeliveryDescription : String, // Information about the delivery process.
        val paymentMethod : String, // DebitCard/CreditCard Mastercard/Visa
        val providerID : String,
        val providerFullName : String,
        val providerAddress : Address
): Parcelable

@Parcelize
data class PurchasedProducts(
    val id: String,
    val title : String,
    val amount : String,
    val priceUnit : String,
    val priceTotal : String
) : Parcelable