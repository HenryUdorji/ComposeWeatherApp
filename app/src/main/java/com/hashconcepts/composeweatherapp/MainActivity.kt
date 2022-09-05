package com.hashconcepts.composeweatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.hashconcepts.composeweatherapp.components.PermissionRationaleDialog
import com.hashconcepts.composeweatherapp.ui.theme.ComposeWeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            ComposeWeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen(fusedLocationProviderClient)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(fusedLocationProviderClient: FusedLocationProviderClient, viewModel: MainViewModel = hiltViewModel()) {
    val homeScreenState = viewModel.homeScreenState
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventChannelFlow.collectLatest { result ->
            when(result) {
                is ResultEvents.ShowMessage -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        result.message,
                        duration = SnackbarDuration.Short
                    )
                }
                else -> {}
            }
        }
    }

    if (homeScreenState.locationPermissionsGranted) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState
        ) {
            Text(
                text = "PERMISSION GRANTED",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        ShowPermissionUI {
            viewModel.onEvents(HomeScreenEvents.OnPermissionGranted(it))
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowPermissionUI(onPermissionGranted: (Boolean) -> Unit) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    val context = LocalContext.current
    var showPermissionRationale by remember { mutableStateOf(true) }
    if (locationPermissionsState.allPermissionsGranted) {
        //Location permission granted
        onPermissionGranted(true)
    } else if (locationPermissionsState.shouldShowRationale) {
        if (showPermissionRationale) {
            PermissionRationaleDialog(
                message = "Compose Weather App requires Location permission to show accurate weather information",
                icon = R.drawable.cloudy,
                onRequestPermission = {
                    showPermissionRationale = false

                    //Launch Settings
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                },
                onDismissRequest = {
                    showPermissionRationale = false
                }
            )
        }
    } else {
        SideEffect {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }
}
