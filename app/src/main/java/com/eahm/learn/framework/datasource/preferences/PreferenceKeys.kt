package com.eahm.learn.framework.datasource.preferences

class PreferenceKeys {

    companion object{

        // Shared Preference Files:
        const val PRODUCT_PREFERENCES: String = "com.eahm.learn.product    "

        // Shared Preference Keys
        val PRODUCT_FILTER: String = "${PRODUCT_PREFERENCES}.PRODUCT_FILTER"
        val PRODUCT_ORDER: String = "${PRODUCT_PREFERENCES}.PRODUCT_ORDER"

    }
}