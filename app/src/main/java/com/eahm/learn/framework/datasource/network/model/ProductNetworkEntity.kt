package com.eahm.learn.framework.datasource.network.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class ProductNetworkImage(
    @PropertyName("photoUrl") val photoUrl : String = "",
    @PropertyName("photoDescription") val photoDescription : String = ""
){
    constructor() : this (
        photoUrl = "",
        photoDescription = ""
    )
}

@IgnoreExtraProperties
data class ProductNetworkTechnicalInfo(
    @PropertyName("size") val size : String = "",
    @PropertyName("weight") val weight : String = "",
    @PropertyName("color") val color : String = "",
    @PropertyName("material") val material : String = "",
    @PropertyName("capacity") val capacity : String = "",
){
    constructor() : this(
        size = "",
        weight = "",
        color = "",
        material = "",
        capacity = ""
    )
}

@IgnoreExtraProperties
data class ProductNetworkEntity (
    @Exclude var id: String,
    @PropertyName("title") val title: String,
    @PropertyName("description") val description: String,
    @PropertyName("photos") val photos : List<ProductNetworkImage> = listOf(),
    @PropertyName("price") val price : Float,
    @PropertyName("technicalInformation") val technicalInformation : Map<String, Any>,
    @PropertyName("provider") val provider : String,
    @PropertyName("reviewTotal") val reviewTotal : Int = 0, // Total of reviews / Everytime a review
    @PropertyName("reviewScore") val reviewScore : Float = 0f, // A value from 1 to 5
    @PropertyName("updated_at") var updated_at: Timestamp,
    @PropertyName("created_at") val created_at: Timestamp
){

    // no arg constructor for firestore
    constructor(): this(
            id ="",
            title ="",
            description ="",
            photos = listOf(),
            price = 1000f,
            technicalInformation = mapOf(),
            provider = "",
            reviewTotal = 0,
            reviewScore = 0f,
            updated_at = Timestamp.now(),
            created_at = Timestamp.now()
    )

    companion object{

        const val UPDATED_AT_FIELD = "updated_at"
        const val TITLE_FIELD = "title"
        const val BODY_FIELD = "body"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductNetworkEntity

        if (id != other.id) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (created_at != other.created_at) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + updated_at.hashCode()
        result = 31 * result + created_at.hashCode()
        return result
    }

}