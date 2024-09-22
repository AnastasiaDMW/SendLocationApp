package com.example.sendlocationapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sendlocationapp.Constant
import com.example.sendlocationapp.dao.LocationDao
import com.example.sendlocationapp.data.UserLocation

@Database(entities = [UserLocation::class], version = 1, exportSchema = false)
abstract class LocationDatabase: RoomDatabase() {

    abstract fun userLocationDao(): LocationDao

    companion object {

        @Volatile
        private var INSTANCE: LocationDatabase? = null

        fun getDatabase(context: Context): LocationDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    LocationDatabase::class.java,
                    Constant.DATABASE
                ).build().also { INSTANCE = it }
            }
        }

        fun getUserLocationDao(context: Context): LocationDao {
            return getDatabase(context).userLocationDao()
        }

    }
}