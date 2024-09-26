package com.example.sendlocationapp.repository

import com.example.sendlocationapp.dao.LocationDao
import com.example.sendlocationapp.data.UserLocation
import com.example.sendlocationapp.fragments.home.UserLocationAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflineLocationRepository @Inject constructor(
    private val locationDao: LocationDao
) {

    suspend fun getAllUserLocation(): List<UserLocation> {
        return withContext(Dispatchers.IO){
            locationDao.getAllUserLocation().first()
        }
    }

    suspend fun addNewLocation(userLocation: UserLocation) {
        withContext(Dispatchers.IO) {
            locationDao.addNewLocation(userLocation)
        }
    }

    suspend fun deleteAllLocation() {
        withContext(Dispatchers.IO) {
            locationDao.deleteAllLocation()
        }
    }

}