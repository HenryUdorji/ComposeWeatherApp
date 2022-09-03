package com.hashconcepts.composeweatherapp.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class WeatherResponse(
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather,
    val elevation: Int,
    @SerializedName("generationtime_ms")
    val generationTimeMs: Double,
    val hourly: Hourly,
    @SerializedName("hourly_units")
    val hourlyUnits: HourlyUnits,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    @SerializedName("timezone_abbreviation")
    val timezone_abbreviation: String,
    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int
)