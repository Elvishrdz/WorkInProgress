package com.eahm.learn.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_cart")
data class ShoppingCartCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id : String,

    @ColumnInfo(name = "productID")
    val productID: String,

    @ColumnInfo(name = "amount")
    val amount : Int,

    /**
     * Contains the products in the shopping cart when there is no user authenticated.
     * If a user closes the session we will restore the shopping cart with the products
     * saved in the phone.
     */
    @ColumnInfo(name = "isLocal")
    val isLocal : Boolean
)