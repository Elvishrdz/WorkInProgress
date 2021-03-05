package com.eahm.learn.framework.presentation.product.state

import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.state.StateEvent

sealed class ProductStateEvent : StateEvent {

    class InsertNewProduct(
        val newProduct : Product
    ) : StateEvent{
        override fun errorInfo(): String = "Failed inserting new product"

        override fun eventName(): String = "InsertNewProduct"

        override fun shouldDisplayProgressBar(): Boolean = true

    }
}