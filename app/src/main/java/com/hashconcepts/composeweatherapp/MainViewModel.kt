package com.hashconcepts.composeweatherapp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hashconcepts.composeweatherapp.remote.WeatherRepository
import com.hashconcepts.composeweatherapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * @created 03/09/2022 - 8:18 PM
 * @project ComposeWeatherApp
 * @author  ifechukwu.udorji
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker,
) : ViewModel() {

    private val eventChannel = Channel<ResultEvents>()
    val eventChannelFlow = eventChannel.receiveAsFlow()

    var homeScreenState by mutableStateOf(HomeScreenState())
        private set

    init {
        viewModelScope.launch {
            if (locationTracker.getLocation() != null) {
                homeScreenState = homeScreenState.copy(locationPermissionsGranted = true,)
                val latitude = locationTracker.getLocation()?.latitude
                val longitude = locationTracker.getLocation()?.longitude

                fetchWeatherData(latitude!!, longitude!!)
            } else {
                homeScreenState = homeScreenState.copy(locationPermissionsGranted = false)
            }
        }
    }

    fun onEvents(events: HomeScreenEvents) {
        when (events) {
            is HomeScreenEvents.OnPermissionGranted -> {
                homeScreenState = homeScreenState.copy(locationPermissionsGranted = events.granted)
                viewModelScope.launch {
                    eventChannel.send(ResultEvents.ShowMessage("Location permission granted"))
                }
            }
        }
    }

    fun fetchWeatherData(latitude: Double, longitude: Double) {
        weatherRepository.fetchWeatherData(latitude, longitude).onEach { result ->
            when (result) {
                is Resource.Loading -> homeScreenState = homeScreenState.copy(loading = true)
                is Resource.Error -> {
                    homeScreenState = homeScreenState.copy(loading = false)
                    eventChannel.send(ResultEvents.ShowMessage(result.message!!))
                }
                is Resource.Success -> {
                    homeScreenState = homeScreenState.copy(loading = false, weatherResponse = result.data)
                }
            }
        }.launchIn(viewModelScope)
    }
}