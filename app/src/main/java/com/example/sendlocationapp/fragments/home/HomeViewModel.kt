package com.example.sendlocationapp.fragments.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sendlocationapp.LocationApplication
import com.example.sendlocationapp.data.DefaultAppContainer
import com.example.sendlocationapp.data.UserLocation
import com.example.sendlocationapp.database.LocationDatabase
import com.example.sendlocationapp.repository.OfflineLocationRepository
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(
    private val application: Application,
    private val offlineLocationRepository: OfflineLocationRepository
): ViewModel() {

    private val _userLocation = MutableLiveData<List<UserLocation>>()
    val userLocation: LiveData<List<UserLocation>> = _userLocation

    fun getAllLocation() {
        viewModelScope.launch {
            try {
                _userLocation.value = offlineLocationRepository.getAllUserLocation()
            } catch (e: IOException) {
                Log.d("DATABASE", "Ошибка в получении данных")
            }
        }
    }

    fun clearAllLocation() {
        viewModelScope.launch {
            try {
                offlineLocationRepository.deleteAllLocation()
            } catch (e: IOException) {
                Log.d("DATABASE", "Ошибка удаления данных")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LocationApplication)
                val locationDao = LocationDatabase.getUserLocationDao(application.applicationContext)
                val appContainer = DefaultAppContainer(locationDao)
                HomeViewModel(
                    application,
                    appContainer.offlineRecipeRepository
                )
            }
        }
    }
}