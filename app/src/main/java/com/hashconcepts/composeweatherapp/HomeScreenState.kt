package com.hashconcepts.composeweatherapp

import com.hashconcepts.composeweatherapp.remote.dto.WeatherResponse

/**
 * @created 05/09/2022 - 11:32 AM
 * @project ComposeWeatherApp
 * @author  ifechukwu.udorji
 */
data class HomeScreenState(
    val locationPermissionsGranted: Boolean = false,
    val loading: Boolean = false,
    val weatherResponse: WeatherResponse? = null
)


sealed class HomeScreenEvents() {
    data class OnPermissionGranted(val granted: Boolean): HomeScreenEvents()
}

sealed class ResultEvents {
    data class OnError(val message: String): ResultEvents()
    data class OnSuccess(val message: String?): ResultEvents()
    data class ShowMessage(val message: String): ResultEvents()
}