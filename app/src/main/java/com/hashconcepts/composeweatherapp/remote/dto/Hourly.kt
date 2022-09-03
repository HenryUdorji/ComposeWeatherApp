package com.hashconcepts.composeweatherapp.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Hourly(
    @SerializedName("relativehumidity_2m")
    val relativehumidity2m: List<Int>,
    @SerializedName("temperature_2m")
    val temperature2m: List<Double>,
    val time: List<String>
)