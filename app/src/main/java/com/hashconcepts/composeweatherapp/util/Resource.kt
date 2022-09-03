package com.hashconcepts.composeweatherapp.util

/**
 * @created 03/09/2022 - 8:25 PM
 * @project ComposeWeatherApp
 * @author  ifechukwu.udorji
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
    class Loading<T>(data: T? = null): Resource<T>(data)
}
