package com.example.sendlocationapp.service

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.sendlocationapp.Constant
import com.example.sendlocationapp.Constant.ACTION_STOP_SERVICE
import com.example.sendlocationapp.Constant.CHANNEL_ID
import com.example.sendlocationapp.Constant.NOTIFICATION_NAME
import com.example.sendlocationapp.Constant.REQUEST_CODE_LOCATION
import com.example.sendlocationapp.Constant.SHARED_PREF_NAME
import com.example.sendlocationapp.data.UserLocation
import com.example.sendlocationapp.database.LocationDatabase
import com.example.sendlocationapp.repository.OfflineLocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class LocationForegroundService: Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    @Inject
    lateinit var offlineLocationRepository: OfflineLocationRepository

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupLocationUpdates()
        startForeground(Constant.NOTIFICATION_ID, createMinimalNotification())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            NOTIFICATION_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun setupLocationUpdates() {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10000)
            .setFastestInterval(5000)
            .setSmallestDisplacement(800f)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    handleLocationUpdate(location)
                }
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } else {
            ActivityCompat.requestPermissions(this as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }

    private fun createMinimalNotification(): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Service Running")
            .setPriority(NotificationCompat.PRIORITY_MIN)

        return notificationBuilder.build()
    }

    private fun handleLocationUpdate(location: Location) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
            val oldLatitude = sharedPreferences.getString("latitude", null)
            val oldLongitude = sharedPreferences.getString("longitude", null)

            if (oldLatitude != location.latitude.toString() && oldLongitude != location.longitude.toString()) {

                val currentDatTime = getCurrentDateTime()
                CoroutineScope(Dispatchers.IO).launch {
                    offlineLocationRepository.addNewLocation(
                        UserLocation(
                            latitude = location.latitude.toString(),
                            longitude = location.longitude.toString(),
                            time = currentDatTime.first,
                            date = currentDatTime.second
                        )
                    )
                }
                sharedPreferences.edit().apply {
                    putString("latitude", location.latitude.toString())
                    putString("longitude", location.longitude.toString())
                    apply()
                }
            }
        } else {
            ActivityCompat.requestPermissions(this as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }

    private fun getCurrentDateTime(): Pair<String, String> {

        val currentDateTime = LocalDateTime.now()
        val date = currentDateTime.let {
            "${it.dayOfMonth}-${it.monthValue}-${it.year}"
        }
        val time = currentDateTime.let {
            "${it.hour}:${it.minute}:${it.second}"
        }
        return Pair(time, date)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP_SERVICE) {
            stopSelf()
            return START_NOT_STICKY
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}