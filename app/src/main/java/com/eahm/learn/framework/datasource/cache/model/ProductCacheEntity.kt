package com.eahm.learn.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductCacheEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title : String,

    @ColumnInfo(name = "description")
    val description : String,

    @ColumnInfo(name = "photos")
    val photos : String, // List saved as JSON string

    @ColumnInfo(name = "price")
    val price : Float,

    @ColumnInfo(name = "technicalInfo")
    val technicalInfo : String, // List saved as JSON string

    @ColumnInfo(name = "provider")
    val provider : String,

    @ColumnInfo(name = "updated_at")
    val updated_at : String,

    @ColumnInfo(name = "created_at")
    val created_at : String,

    @ColumnInfo(name = "amount_available")
    val amountAvailable : Int,

    @ColumnInfo(name = "status")
    val status : String

)