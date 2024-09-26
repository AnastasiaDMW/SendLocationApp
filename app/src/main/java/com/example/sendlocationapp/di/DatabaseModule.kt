package com.example.sendlocationapp.di

import android.content.Context
import androidx.room.Room
import com.example.sendlocationapp.Constant
import com.example.sendlocationapp.dao.LocationDao
import com.example.sendlocationapp.database.LocationDatabase
import com.example.sendlocationapp.repository.OfflineLocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideLocationDao(database: LocationDatabase): LocationDao {
        return database.userLocationDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): LocationDatabase {
        return Room.databaseBuilder(
            appContext,
            LocationDatabase::class.java,
            Constant.DATABASE
        ).build()
    }

    @Provides
    fun provideOfflineLocationRepository(locationDao: LocationDao): OfflineLocationRepository {
        return OfflineLocationRepository(locationDao)
    }
}