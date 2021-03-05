package com.eahm.learn.framework.datasource.cache.dao

import androidx.room.*
import com.eahm.learn.framework.datasource.cache.model.UserCacheEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(newUser : UserCacheEntity) : Long

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String) : Int

    @Query("DELETE FROM users")
    suspend fun deleteUsers() : Int

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUser(userId : String) : UserCacheEntity?
}