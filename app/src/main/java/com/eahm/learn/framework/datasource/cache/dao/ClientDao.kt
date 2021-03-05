package com.eahm.learn.framework.datasource.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eahm.learn.framework.datasource.cache.model.ClientCacheEntity

@Dao
interface ClientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(newUser : ClientCacheEntity) : Long

    @Query("DELETE FROM client")
    suspend fun deleteClient() : Int

    @Query("SELECT * FROM client WHERE id = :userId")
    suspend fun getClient(userId : String) : ClientCacheEntity?
}