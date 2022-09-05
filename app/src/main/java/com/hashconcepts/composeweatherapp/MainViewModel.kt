package com.hashconcepts.composeweatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hashconcepts.composeweatherapp.remote.WeatherRepository
import com.hashconcepts.composeweatherapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * @created 03/09/2022 - 8:18 PM
 * @project ComposeWeatherApp
 * @author  ifechukwu.udorji
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
): ViewModel() {

    init {
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        weatherRepository.fetchWeatherData(6.54, 3.35).onEach { result ->
            when(result) {
                is Resource.Loading -> Log.d("OBSERVE ::::::::::::::", "LOADING")
                is Resource.Error -> Log.d("OBSERVE ::::::::::::::", "${result.message}")
                is Resource.Success -> Log.d("OBSERVE ::::::::::::::", "${result.data}")
            }
        }.launchIn(viewModelScope)
    }
}