package com.eahm.learn.business.interactors.productlist

import com.eahm.learn.business.data.cache.abstraction.ProductCacheDataSource
import com.eahm.learn.business.data.network.abstraction.ProductNetworkDataSource

class RestoreDeletedProduct(
    productCacheDataSource: ProductCacheDataSource,
    productNetworkDataSource: ProductNetworkDataSource
) {
}