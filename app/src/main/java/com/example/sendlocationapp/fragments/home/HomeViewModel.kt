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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
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
}