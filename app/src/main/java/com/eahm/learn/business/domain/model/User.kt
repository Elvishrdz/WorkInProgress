package com.eahm.learn.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id : String,
    val name_first : String,
    val name_second : String,
    val last_name_first : String,
    val last_name_second : String,
    val email : String,
    val addresses : List<Address>,
    val phoneNumber : List<Phone>,
    val dateBirth : String,
    val providerId : String,
    val created_at : String,
    val status : String
) : Parcelable {

    fun isValid(): Boolean = id.isNotEmpty() &&
            name_first.isNotEmpty() &&
            last_name_first.isNotEmpty() &&
            (status.isNotEmpty() || status != "inactive")

}

@Parcelize
data class Address(
    val street : String,
    val buildingNumber : String,
    val postCode : String,
    val countryCode : String,
    val description : String,
    //TODO add recipient name. Who will receive this package. Save on Server, dont provide userID just sending instructions. we can track order with the OrderID
    // TODO add instructions in case no one is at home.
    val geolocation : Location
) : Parcelable

@Parcelize
data class Phone(
    val extension : String,
    val number : String,
    val description : String // Whatsapp/Mobile/HousePhone/SecondNumber
) : Parcelable

