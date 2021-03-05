package com.eahm.learn.framework.datasource.cache.dao

import androidx.room.*
import com.eahm.learn.framework.datasource.cache.model.BusinessCacheEntity

@Dao
interface BusinessDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusiness(newBusiness: BusinessCacheEntity) : Long

    @Query("UPDATE business SET name = :name, status = :status WHERE id = :businessId ")
    suspend fun updateBusiness(businessId : String, name : String, status : String) : Int

    @Query("DELETE FROM business WHERE id = :businessId")
    suspend fun deleteBusiness(businessId: String) : Int

    @Query("SELECT * FROM business WHERE id = :businessId")
    suspend fun getBusiness(businessId : String) : BusinessCacheEntity?

}