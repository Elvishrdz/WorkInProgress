package com.eahm.learn.business.interactors.productlist

import com.eahm.learn.business.interactors.common.DeleteProduct
import com.eahm.learn.business.interactors.common.GetShoppingCartItemAmountNum
import com.eahm.learn.framework.presentation.productlist.state.ProductListViewState

class ProductListInteractors(
    val insertNewProduct: InsertNewProduct,
    val deleteProduct: DeleteProduct<ProductListViewState>,
    val searchProducts: SearchProducts,
    val getNumProducts: GetNumProducts,
    val restoreDeletedProduct: RestoreDeletedProduct,
    val deleteMultipleProducts: DeleteMultipleProducts,
    val getShoppingCartItemAmountNum : GetShoppingCartItemAmountNum<ProductListViewState>
)