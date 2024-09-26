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

}