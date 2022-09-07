package com.hashconcepts.composeweatherapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.fonts.FontStyle
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.hashconcepts.composeweatherapp.components.PermissionRationaleDialog
import com.hashconcepts.composeweatherapp.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeWeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(viewModel: MainViewModel = hiltViewModel()) {
    val homeScreenState = viewModel.homeScreenState
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventChannelFlow.collectLatest { result ->
            when (result) {
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
            scaffoldState = scaffoldState,
            backgroundColor = Color.Black
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                CurrentWeatherSection()
            }
        }
    } else {
        ShowPermissionUI {
            viewModel.onEvents(HomeScreenEvents.OnPermissionGranted(it))
        }
    }
}

@Composable
fun CurrentWeatherSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(0f to PrimaryLight, 1000f to PrimaryDark),
                shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
            )
    ) {
        Text(
            text = "Minsk",
            style = MaterialTheme.typography.body1,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(id = R.drawable.ic_cloudy),
            contentDescription = null,
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
        )

        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = "21",
                style = MaterialTheme.typography.h1,
                fontSize = 100.sp,
                color = Color.White,
            )
            Text(
                text = "°C",
                style = MaterialTheme.typography.h1,
                fontSize = 40.sp,
                color = Color.White,
            )
        }

        Text(
            text = "ThunderStorm",
            style = MaterialTheme.typography.body1,
            fontSize = 20.sp,
            color = Color.White
        )

        Text(
            text = "Monday, 17 May",
            style = MaterialTheme.typography.body1,
            fontSize = 12.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(OffWhite)
                .padding(horizontal = 20.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            CurrentWeatherItem(icon = R.drawable.ic_wind, label = "Wind", value = "13 km/h")
            CurrentWeatherItem(icon = R.drawable.ic_drop, label = "Humidity", value = "24%")
            CurrentWeatherItem(icon = R.drawable.ic_pressure, label = "Chance of rain", value = "87%")
        }
    }
}

@Composable
fun CurrentWeatherItem(
    icon: Int,
    label: String,
    value: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(25.dp),
            tint = Color.White
        )
        Text(text = value, style = MaterialTheme.typography.body1, fontSize = 12.sp, color = Color.White)
        Text(text = label, style = MaterialTheme.typography.body1, fontSize = 12.sp, color = OffWhite)
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
