package com.eahm.learn.framework.datasource.network.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName


@IgnoreExtraProperties
data class OrderNetworkEntity(
        @DocumentId val id : String,
        @PropertyName("delivery") var delivery : DeliveryNetworkEntity,
        @PropertyName("order_date") var order_date : String,
        @PropertyName("products") var products : List<OrderProductEntityList>,
        @PropertyName("clientId") var clientId : String, // Which account did the order
        @PropertyName("orderOrigin") var orderOrigin : String, // Device. (phone, laptop, etc)
        @PropertyName("status") var status : String
){
    constructor() : this(
            id = "",
            delivery = DeliveryNetworkEntity(),
            order_date = "",
            products = listOf(),
            clientId = "",
            orderOrigin = "",
            status = ""
    )
}

@IgnoreExtraProperties
data class DeliveryNetworkEntity(
    @PropertyName("address") val address : String,
    @PropertyName("city") val city : String,
    @PropertyName("country") val country : String,
    @PropertyName("description") val description : String,
    @PropertyName("postCode") val postCode : String,
    @PropertyName("town") val town : String
) {
    constructor() : this(
        address = "",
        city = "",
        country = "",
        description = "",
        postCode = "",
        town = "",
    )
}


@IgnoreExtraProperties
data class OrderProductEntityList(
    @PropertyName("amount") val amount : Int,
    @PropertyName("productId") val productId : String, // Reference to the product
    @PropertyName("orderId") var orderId : String = "" // Reference to the orderId where the productId belongs
) {
    constructor() : this(
            amount = 0,
            productId = "",
            orderId = "",
    )
}
