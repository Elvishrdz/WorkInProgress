package com.eahm.learn.framework.datasource.cache.model.provider

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_provider")
data class ProductProviderCacheEntity (

    @PrimaryKey(autoGenerate = false)
    val id : String,

    @ColumnInfo(name = "productId")
    val productId : String
)