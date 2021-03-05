package com.eahm.learn.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    val latitud : Double = 0.0,
    val longitud : Double = 0.0
) : Parcelable {
}