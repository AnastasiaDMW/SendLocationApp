package com.example.sendlocationapp

import android.app.Application
import android.content.Intent
import android.os.Build
import com.example.sendlocationapp.data.DefaultAppContainer
import com.example.sendlocationapp.database.LocationDatabase
import com.example.sendlocationapp.service.LocationForegroundService

class LocationApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        val locationDao = LocationDatabase.getUserLocationDao(this)
        DefaultAppContainer(locationDao)
    }

}