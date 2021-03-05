package com.eahm.learn.business.interactors.productdetail

import com.eahm.learn.business.interactors.common.business.SearchBusiness
import com.eahm.learn.business.interactors.common.DeleteProduct
import com.eahm.learn.business.interactors.common.InsertProductInCart
import com.eahm.learn.business.interactors.common.business.InsertBusiness
import com.eahm.learn.business.interactors.common.provider.InsertProvider
import com.eahm.learn.business.interactors.common.provider.SearchProvider
import com.eahm.learn.business.interactors.common.sync.SyncProvider
import com.eahm.learn.business.interactors.common.user.InsertUser
import com.eahm.learn.business.interactors.shoppingcart.SearchProductInCart
import com.eahm.learn.business.interactors.common.user.SearchUser
import com.eahm.learn.framework.presentation.productdetail.state.ProductDetailViewState

class ProductDetailInteractors(
    val deleteProduct : DeleteProduct<ProductDetailViewState>,
    val insertProductInCart: InsertProductInCart<ProductDetailViewState>,
    val searchProductInCart: SearchProductInCart<ProductDetailViewState>,

    val syncProvider: SyncProvider
)