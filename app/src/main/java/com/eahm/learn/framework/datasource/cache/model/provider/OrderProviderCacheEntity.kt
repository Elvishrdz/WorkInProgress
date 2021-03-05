package com.eahm.learn.framework.datasource.cache.model.provider

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eahm.learn.framework.datasource.network.model.UserAddressNetworkEntity

// ACTIVE ORDERS
@Entity(tableName = "orders_provider")
data class OrderProviderCacheEntity(

    @PrimaryKey(autoGenerate = false)
    val id : String,

    val address: String,  // Save JSON string

    @ColumnInfo(name = "productId")
    val productId: String
)