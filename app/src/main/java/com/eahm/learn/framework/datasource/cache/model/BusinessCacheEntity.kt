package com.eahm.learn.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "business")
data class BusinessCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id : String,

    @ColumnInfo(name = "name")
    val name : String,

    @ColumnInfo(name = "status")
    val status : String
)