package com.eahm.learn.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: String,
    val title: String,
    val description: String,
    val photos : List<ProductImage> = listOf(),
    val price : Float,
    val technicalInfo : ProductTechnicalInfo? = ProductTechnicalInfo(),
    val provider : Provider,
    val reviewTotal : Int = 0, // Total of reviews / Everytime a review
    val reviewScore : Float = 0f, // A value from 1 to 5
    val updated_at: String,
    val created_at: String,
    val amountAvailable : Int, // In Stock
    val status : String // available/removed/banned/private
) : Parcelable


@Parcelize
data class ProductImage(
    val photoUrl : String,
    val photoDescription : String
) : Parcelable

@Parcelize
data class ProductTechnicalInfo (
    val size : String = "",
    val weight : String = "",
    val color : String = "",
    val material : String = "",
    val capacity : String = "",
    val value : Int = 123
) : Parcelable