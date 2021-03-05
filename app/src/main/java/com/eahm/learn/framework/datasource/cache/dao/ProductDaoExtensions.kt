package com.eahm.learn.framework.datasource.cache.dao

import com.eahm.learn.framework.datasource.cache.model.ProductCacheEntity

//region constants
const val PRODUCT_DEFAULT_QUERY = "none"
const val PRODUCT_DEFAULT_PAGE = 1

const val PRODUCT_ORDER_ASC: String = "+"
const val PRODUCT_ORDER_DESC: String = "-"

const val PRODUCT_FILTER_TITLE = "title"
const val PRODUCT_FILTER_DATE_CREATED = "created_at"

const val ORDER_BY_ALL = "all"
const val ORDER_BY_TITLE_ASC = PRODUCT_FILTER_TITLE + PRODUCT_ORDER_ASC
const val ORDER_BY_TITLE_DESC =  PRODUCT_FILTER_TITLE + PRODUCT_ORDER_DESC
const val ORDER_BY_DATE_UPDATED_ASC =  PRODUCT_FILTER_DATE_CREATED + PRODUCT_ORDER_ASC
const val ORDER_BY_DATE_UPDATED_DESC =   PRODUCT_FILTER_DATE_CREATED + PRODUCT_ORDER_DESC

const val PRODUCT_PAGINATION_PAGE_SIZE = 30
//endregion constants

suspend fun ProductDao.searchProducts (
    query: String,
    filter: String,
    order : String,
    page: Int
) : List<ProductCacheEntity>{

    val filterAndOrder = filter + order

    return when{
        filterAndOrder.contains(ORDER_BY_ALL) -> {
            getProducts()
        }

        filterAndOrder.contains(ORDER_BY_DATE_UPDATED_ASC) ->{
            getProductsOrderByDateASC(
                query = query,
                page = page)
        }

        filterAndOrder.contains(ORDER_BY_DATE_UPDATED_DESC) ->{
            getProductsOrderByDateDESC(
                query = query,
                page = page)
        }

        filterAndOrder.contains(ORDER_BY_TITLE_ASC) ->{
            getProductsOrderByTitleASC(
                query = query,
                page = page
            )
        }

        filterAndOrder.contains(ORDER_BY_TITLE_DESC) ->{
            getProductsOrderByTitleDESC(
                query = query,
                page = page
            )
        }

        else -> {
            getProductsOrderByDateASC(
                query = query,
                page = page)
        }
    }
}
