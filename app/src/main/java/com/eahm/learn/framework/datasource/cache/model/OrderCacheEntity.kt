package com.eahm.learn.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderCacheEntity (

    @PrimaryKey(autoGenerate = false)
    val id : String,

    @ColumnInfo(name = "delivery")
    var delivery : String,

    @ColumnInfo(name = "order_date")
    var order_date : String,

    @ColumnInfo(name = "products")
    var products : String,

    @ColumnInfo(name = "clientId")
    var clientId : String, // Which account did the order

    @ColumnInfo(name = "orderOrigin")
    var orderOrigin : String, // Device. (phone, laptop, etc)

    @ColumnInfo(name = "status")
    var status : String

)