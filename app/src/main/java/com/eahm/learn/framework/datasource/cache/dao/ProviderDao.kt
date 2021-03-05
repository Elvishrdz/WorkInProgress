package com.eahm.learn.framework.datasource.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eahm.learn.framework.datasource.cache.model.ProviderCacheEntity

@Dao
interface ProviderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvider(newProvider : ProviderCacheEntity) : Long

    @Query("DELETE FROM providers WHERE id = :providerId")
    suspend fun deleteProvider(providerId: String) : Int

    @Query("UPDATE providers SET businessId = :businessId, ownerUserId = :ownerUserId WHERE id = :providerId")
    suspend fun updateProvider(providerId : String, businessId :String, ownerUserId : String) : Int

    @Query("SELECT * FROM providers WHERE id = :providerId")
    suspend fun getProvider(providerId : String) : ProviderCacheEntity?

}