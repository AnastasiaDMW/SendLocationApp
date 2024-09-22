package com.example.sendlocationapp.data

import com.example.sendlocationapp.dao.LocationDao
import com.example.sendlocationapp.repository.OfflineLocationRepository


class DefaultAppContainer(
    private val locationDao: LocationDao,
) {

    val offlineRecipeRepository: OfflineLocationRepository by lazy {
        OfflineLocationRepository(locationDao)
    }
}