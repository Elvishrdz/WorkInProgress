package com.eahm.learn.business.data.cache

sealed class CacheResult <out T> {
    // wrapper class for the results

    data class Success<out T>(val value : T) : CacheResult<T>()

    data class GenericError(val errorMessage : String? = null) : CacheResult<Nothing>()

}