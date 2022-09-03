package com.hashconcepts.composeweatherapp.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class HourlyUnits(
    @SerializedName("relativehumidity_2m")
    val relativehumidity2m: String,
    @SerializedName("temperature_2m")
    val temperature2m: String,
    val time: String
)