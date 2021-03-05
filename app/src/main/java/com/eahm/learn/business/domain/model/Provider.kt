package com.eahm.learn.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Provider(
        val id : String,
        val business : Business?,
        val user : User?
) : Parcelable

