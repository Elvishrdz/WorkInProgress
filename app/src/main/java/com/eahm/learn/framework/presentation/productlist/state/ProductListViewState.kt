package com.eahm.learn.framework.presentation.productlist.state

import android.os.Parcelable
import com.eahm.learn.business.domain.model.Product
import com.eahm.learn.business.domain.state.ViewState
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductListViewState(

    var productList: ArrayList<Product>? = null,
    var newProduct: Product? = null, // product that can be created with fab
    var productPendingDelete: ProductPendingDelete? = null, // set when delete is pending (can be undone)
    var searchQuery: String? = null,
    var page: Int? = null,
    var isQueryExhausted: Boolean? = null,
    var filter: String? = null,
    var order: String? = null,
    var layoutManagerState: Parcelable? = null,
    var numProductsInCache: Int? = null,
    var numItemsInCart : Int? = null

) : Parcelable, ViewState {

    @Parcelize
    data class ProductPendingDelete(
        var product: Product? = null,
        var listPosition: Int? = null
    ) : Parcelable
}