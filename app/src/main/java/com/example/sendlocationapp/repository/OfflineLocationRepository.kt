package com.example.sendlocationapp.repository

import com.example.sendlocationapp.dao.LocationDao
import com.example.sendlocationapp.data.UserLocation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class OfflineLocationRepository(
    private val locationDao: LocationDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getAllUserLocation(): List<UserLocation> {
        return withContext(ioDispatcher){
            locationDao.getAllUserLocation().first()
        }
    }

    suspend fun deleteAllLocation() {
        withContext(ioDispatcher) {
            locationDao.deleteAllLocation()
        }
    }

}