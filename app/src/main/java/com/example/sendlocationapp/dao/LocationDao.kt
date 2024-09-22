package com.example.sendlocationapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sendlocationapp.data.UserLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query("SELECT * FROM userlocation")
    fun getAllUserLocation(): Flow<List<UserLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNewLocation(userLocation: UserLocation)

    @Query("DELETE FROM userlocation")
    suspend fun deleteAllLocation()
}