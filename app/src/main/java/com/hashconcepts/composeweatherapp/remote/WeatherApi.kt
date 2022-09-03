package com.hashconcepts.composeweatherapp.remote

import com.hashconcepts.composeweatherapp.remote.dto.WeatherResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @created 03/09/2022 - 1:22 PM
 * @project ComposeWeatherApp
 * @author  ifechukwu.udorji
 */
interface WeatherApi {
    @GET("v1/forecast")
    suspend fun fetchWeatherData(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String = "temperature_2m",
        @Query("current_weather") currentWeather: Boolean = true,
    ): Response<WeatherResponse>
}