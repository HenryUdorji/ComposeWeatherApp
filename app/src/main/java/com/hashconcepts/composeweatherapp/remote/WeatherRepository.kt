package com.hashconcepts.composeweatherapp.remote

import com.hashconcepts.composeweatherapp.remote.dto.WeatherResponse
import com.hashconcepts.composeweatherapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * @created 03/09/2022 - 8:20 PM
 * @project ComposeWeatherApp
 * @author  ifechukwu.udorji
 */
class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi
) {
    fun fetchWeatherData(
        latitude: Double,
        longitude: Double,
    ): Flow<Resource<WeatherResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = weatherApi.fetchWeatherData(latitude, longitude)
            emit(Resource.Success(data = response.body()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}