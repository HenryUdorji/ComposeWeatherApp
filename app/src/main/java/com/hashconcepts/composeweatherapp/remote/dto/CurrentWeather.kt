package com.hashconcepts.composeweatherapp.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CurrentWeather(
    val temperature: Double,
    val time: String,
    @SerializedName("weathercode")
    val weatherCode: Int,
    @SerializedName("winddirection")
    val windDirection: Int,
    @SerializedName("windspeed")
    val windSpeed: Double
)