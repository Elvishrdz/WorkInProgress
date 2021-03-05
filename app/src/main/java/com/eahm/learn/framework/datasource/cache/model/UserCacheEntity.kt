package com.eahm.learn.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserCacheEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id : String,

    @ColumnInfo(name = "name_first")
    val name_first : String,

    @ColumnInfo(name = "name_second")
    val name_second : String,

    @ColumnInfo(name = "last_name_first")
    val last_name_first : String,

    @ColumnInfo(name = "last_name_second")
    val last_name_second : String,

    @ColumnInfo(name = "addresses")
    val addresses : String, // Save JSON string

    @ColumnInfo(name = "phoneNumber")
    val phoneNumber : String, // Save JSON string

    @ColumnInfo(name = "dateBirth")
    val dateBirth : String,

    @ColumnInfo(name = "providerId")
    val providerId : String,

    @ColumnInfo(name = "created_at")
    val created_at : String,

    @ColumnInfo(name = "status")
    val status : String

)


@Entity(tableName = "client")
data class ClientCacheEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id : String,

    @ColumnInfo(name = "name_first")
    val name_first : String,

    @ColumnInfo(name = "name_second")
    val name_second : String,

    @ColumnInfo(name = "last_name_first")
    val last_name_first : String,

    @ColumnInfo(name = "last_name_second")
    val last_name_second : String,

    @ColumnInfo(name = "addresses")
    val addresses : String, // Save JSON string

    @ColumnInfo(name = "phoneNumber")
    val phoneNumber : String, // Save JSON string

    @ColumnInfo(name = "dateBirth")
    val dateBirth : String,

    @ColumnInfo(name = "providerId")
    val providerId : String,

    @ColumnInfo(name = "created_at")
    val created_at : String,

    @ColumnInfo(name = "status")
    val status : String

)