package com.eahm.learn.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "providers")
data class ProviderCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id : String,

    @ColumnInfo(name = "businessId")
    val businessId : String,

    @ColumnInfo(name = "ownerUserId")
    val ownerUserId : String
)