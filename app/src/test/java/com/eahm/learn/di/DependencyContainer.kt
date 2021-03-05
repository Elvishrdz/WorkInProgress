package com.eahm.learn.di

import com.eahm.learn.business.domain.factory.ProductFactory
import com.eahm.learn.business.domain.util.DateUtil
import com.eahm.learn.business.data.cache.abstraction.ProductCacheDataSource
import com.eahm.learn.business.data.network.FakeProductCacheDataSourceImpl
import com.eahm.learn.business.data.network.FakeProductNetworkDataSourceImpl
import com.eahm.learn.business.data.network.abstraction.ProductNetworkDataSource
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class DependencyContainer {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
    val dateUtil = DateUtil(dateFormat)
    lateinit var ProductNetworkDataSource: ProductNetworkDataSource
    lateinit var ProductCacheDataSource: ProductCacheDataSource
    lateinit var ProductFactory: ProductFactory

    init {
       // isUnitTest = true // for Logger.kt
    }

    fun build() {
        ProductFactory = ProductFactory(dateUtil)
        ProductNetworkDataSource = FakeProductNetworkDataSourceImpl(
            ProductsData = HashMap(),
            deletedProductsData = HashMap()
        )
        ProductCacheDataSource = FakeProductCacheDataSourceImpl(
            ProductsData = HashMap(),
            dateUtil = dateUtil
        )
    }

}