package com.eahm.learn.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.eahm.learn.framework.presentation.BaseApplication
import com.eahm.learn.framework.datasource.cache.AppDatabase
import com.eahm.learn.framework.datasource.preferences.PreferenceKeys
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

/*
    Dependencies in this class have test fakes for ui tests. See "TestModule.kt" in
    androidTest dir
 */
@ExperimentalCoroutinesApi
@FlowPreview
@Module
object ProductionModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideAppDatabase(app: BaseApplication): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSharePreferences(
        application : BaseApplication
    ) : SharedPreferences {
        return application
            .getSharedPreferences(
                PreferenceKeys.PRODUCT_PREFERENCES,
                Context.MODE_PRIVATE
            )
    }

}