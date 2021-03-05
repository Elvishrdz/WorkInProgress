package com.eahm.learn.business.domain.model.pending

import android.os.Parcelable
import com.eahm.learn.business.domain.model.ProductImage
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Review(
    val id : String,
    val productID : String,
    val userID : String,
    val amount : Int,
    val message : String,
    val photo : List<ProductImage>,
    val updated_at : String,
    val created_at : String
) : Parcelable