package com.eahm.learn.framework.datasource.network.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class BusinessNetworkEntity (
    @DocumentId // must be used in a VAL variable (not mutable)
    val id : String,

    @PropertyName("name")
    val name : String,

    @PropertyName("status")
    val status : String
){
    constructor() : this(
        id = "",
        name = "",
        status = ""
    )
}