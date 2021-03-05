package com.eahm.learn.framework.datasource.network.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class ShoppingCartNetworkEntity(
    @DocumentId
    val id : String,

    @PropertyName("productId")
    val productId: String,

    @PropertyName("amount")
    val amount : Int
) {
    constructor() : this(
        id = "",
        productId = "",
        amount = 0
    )
}