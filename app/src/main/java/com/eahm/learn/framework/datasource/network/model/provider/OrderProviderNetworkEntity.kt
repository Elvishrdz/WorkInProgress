package com.eahm.learn.framework.datasource.network.model.provider

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class OrderProviderNetworkEntity(
        @Exclude var id : String,
        @PropertyName("address") val address : AddressNetworkEntity,
        @PropertyName("productId") val productId : String,
        @PropertyName("productOrdered") val productOrdered : String
){
    constructor() : this (
            "",
            productId = "",
            address = AddressNetworkEntity(),
            productOrdered = ""
    )
}

@IgnoreExtraProperties
data class AddressNetworkEntity(
    @PropertyName("address") val address : String,
    @PropertyName("city") val city : String,
    @PropertyName("country") val country : String,
    @PropertyName("description") val description : String,
    @PropertyName("postCode") val postCode : String,
    @PropertyName("town") val town : String
){
    constructor() : this (
        address = "",
        city = "",
        country = "",
        description = "",
        postCode = "",
        town = ""
    )
}


