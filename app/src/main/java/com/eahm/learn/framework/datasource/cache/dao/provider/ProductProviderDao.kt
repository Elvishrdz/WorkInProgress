package com.eahm.learn.framework.datasource.cache.dao.provider

import androidx.room.*
import com.eahm.learn.framework.datasource.cache.model.provider.ProductProviderCacheEntity

@Dao
interface ProductProviderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductProvider(newProductProvider : ProductProviderCacheEntity) : Long

    @Query("SELECT * FROM product_provider")
    suspend fun getAllProductsProvider() : List<ProductProviderCacheEntity>?

    @Query("DELETE FROM product_provider")
    suspend fun deleteProductsProvider() : Int

}