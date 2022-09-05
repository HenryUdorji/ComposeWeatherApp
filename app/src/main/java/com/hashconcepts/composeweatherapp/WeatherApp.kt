package com.hashconcepts.composeweatherapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * @created 03/09/2022 - 1:15 PM
 * @project ComposeWeatherApp
 * @author  ifechukwu.udorji
 */
@HiltAndroidApp
class WeatherApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}