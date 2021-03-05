package com.eahm.learn.framework.datasource.network.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.*

@IgnoreExtraProperties
data class UserAddressNetworkEntity(
    @PropertyName("street") val street : String,
    @PropertyName("buildingNumber") val buildingNumber : String,
    @PropertyName("postCode") val postCode : String,
    @PropertyName("countryCode") val countryCode : String,
    @PropertyName("description") val description : String,
    @PropertyName("geolocation") val geolocation : GeoPoint
) {
    constructor() : this (
        street = "",
        buildingNumber = "",
        postCode = "",
        countryCode = "",
        description = "",
        geolocation = GeoPoint(0.0,0.0)
    )
}

@IgnoreExtraProperties
data class UserPhoneNetworkEntity(
    @PropertyName("extension") val extension : String,
    @PropertyName("number") val number : String,
    @PropertyName("description") val description : String // Whatsapp/Mobile/HousePhone/SecondNumber
){
    constructor() : this(
        extension = "",
        number = "",
        description = "")
}

@IgnoreExtraProperties
data class UserNetworkEntity(
    // must be used in a VAL variable (not mutable)
    @DocumentId val id : String,
    @PropertyName("name_first") val name_first : String,
    @PropertyName("name_second")val name_second : String,
    @PropertyName("last_name_first") val last_name_first : String,
    @PropertyName("last_name_second") val last_name_second : String,
    @PropertyName("addresses") val addresses : List<UserAddressNetworkEntity>,
    @PropertyName("phoneNumber") val phoneNumber : List<UserPhoneNetworkEntity>,
    @PropertyName("date_birth") val date_birth : Timestamp,
    @PropertyName("providerId") val providerId : String,
    @PropertyName("created_at") val created_at : Timestamp,
    @PropertyName("status") val status : String
){
    constructor() : this(
        id = "",
        name_first =  "",
        name_second = "",
        last_name_first = "",
        last_name_second = "",
        addresses = listOf(),
        phoneNumber = listOf(),
        date_birth = Timestamp.now(),
        providerId = "",
        created_at = Timestamp.now(),
        status = ""
    )
}