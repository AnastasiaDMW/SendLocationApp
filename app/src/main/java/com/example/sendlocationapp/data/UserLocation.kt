package com.example.sendlocationapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude: String,
    val longitude: String,
    val time: String,
    val date: String
)