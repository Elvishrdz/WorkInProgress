package com.eahm.learn.framework.datasource.network.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class ProviderNetworkEntity(
    @DocumentId // must be used in a VAL variable (not mutable)
    val id: String,

    @PropertyName("businessId")
    val businessId: String,

    @PropertyName("ownerUserId")
    val ownerUserId: String
){
    constructor() : this(
        id = "",
        businessId = "",
        ownerUserId = ""
    )

    constructor(id: String, businessId: String, ownerUserId: String, ignore: String) : this(
        id = id,
        businessId = businessId,
        ownerUserId = ownerUserId
    )

}