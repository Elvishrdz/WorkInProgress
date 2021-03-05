package com.eahm.learn.framework.datasource.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eahm.learn.framework.datasource.cache.dao.*
import com.eahm.learn.framework.datasource.cache.dao.provider.OrderProviderDao
import com.eahm.learn.framework.datasource.cache.dao.provider.ProductProviderDao
import com.eahm.learn.framework.datasource.cache.model.*
import com.eahm.learn.framework.datasource.cache.model.provider.OrderProviderCacheEntity
import com.eahm.learn.framework.datasource.cache.model.provider.ProductProviderCacheEntity

@Database(
    entities = [
        ProductCacheEntity::class,
        ShoppingCartCacheEntity::class,
        ProviderCacheEntity::class,
        BusinessCacheEntity::class,
        ClientCacheEntity::class,
        UserCacheEntity::class,
        OrderProviderCacheEntity::class,
        OrderCacheEntity::class,
        ProductProviderCacheEntity::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun shoppingCartDao() : ShoppingCartDao
    abstract fun providerDao() : ProviderDao
    abstract fun businessDao() : BusinessDao
    abstract fun userDao() : UserDao
    abstract fun clientDao() : ClientDao
    abstract fun orderProviderDao() : OrderProviderDao
    abstract fun orderDao() : OrderDao
    abstract fun productProviderDao() : ProductProviderDao

    companion object{
        val DATABASE_NAME: String = "myApp_db"
    }

}