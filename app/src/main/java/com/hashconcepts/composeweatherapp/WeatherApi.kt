package com.hashconcepts.composeweatherapp

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
    @GET("https://api.open-meteo.com/v1/forecast")
    suspend fun fetchWeatherData(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String = "temperature_2m"
    ): Response<ResponseBody>
}