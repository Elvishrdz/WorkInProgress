package com.eahm.learn.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Business(
    val id : String,
    val name : String,
    val status : String
) : Parcelable {

    fun isValid(): Boolean=  id.isNotEmpty() &&
            name.isNotEmpty() &&
            (status.isNotEmpty() || status != "inactive")

}