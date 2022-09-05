package com.hashconcepts.composeweatherapp

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject

/**
 * @created 05/09/2022 - 6:28 PM
 * @project ComposeWeatherApp
 * @author  ifechukwu.udorji
 */
@ExperimentalCoroutinesApi
class LocationTrackerImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val application: Application
) : LocationTracker {
    override suspend fun getLocation(): Location? {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!hasFineLocationPermission || !hasCoarseLocationPermission || !gpsEnabled) {
            return null
        }


        val priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
        val cancellationTokenSource = CancellationTokenSource()
        return suspendCancellableCoroutine { cont ->
            fusedLocationProviderClient.getCurrentLocation(priority, cancellationTokenSource.token).apply {
                if(isComplete) {
                    if(isSuccessful) {
                        cont.resume(result) { Timber.e(it) }
                    } else {
                        cont.resume(null) { Timber.e(it) }
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(it, null)
                }
                addOnFailureListener {
                    cont.resume(null) { Timber.e(it) }
                }
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }

}

interface LocationTracker {
    suspend fun getLocation(): Location?
}