package com.tappytaps.storky

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.google.android.gms.ads.MobileAds
import com.tappytaps.storky.navigation.StorkyNavigation
import com.tappytaps.storky.screens.home.HomeScreenViewModel
import com.tappytaps.storky.ui.theme.StorkyTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeScreenViewModel by viewModels()
    private lateinit var stopwatchUpdateReceiver: BroadcastReceiver
    private var isReceiverRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (!isTablet(this)) { //to determine if the device is a tablet
            requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
        MobileAds.initialize(this) {}
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = android.graphics.Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )


        setContent {
            StorkyTheme {
                StorkyApp(intent)
            }


        }
        askPermissionPostNotification()



        stopwatchUpdateReceiver =
            object : BroadcastReceiver() { //receiver for communication between Service and Activity
                override fun onReceive(context: Context?, intent: Intent?) {
                    val currentLengthBetweenContractions =
                        intent?.getIntExtra("currentLengthBetweenContractions", 0) ?: 0
                    val pauseStopWatch = intent?.getBooleanExtra("pauseStopWatch", false) ?: false
                    val showContractionlScreen =
                        intent?.getBooleanExtra("showContractionlScreen", false) ?: false
                    val currentContractionLength =
                        intent?.getIntExtra("currentContractionLength", 0) ?: 0
                    Log.d("StorkyService:", "onReceive")
                    homeViewModel.updateFromService(
                        currentLengthBetweenContractions,
                        pauseStopWatch,
                        showContractionlScreen,
                        currentContractionLength
                    )
                }
            }
    }


    override fun onResume() {
        Log.d("StorkyService:", "onResume_in_Maint_activity")
        super.onResume()
        homeViewModel.stopService(this)
        if (!isReceiverRegistered) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                registerReceiver(
                    stopwatchUpdateReceiver,
                    IntentFilter("STOPWATCH_UPDATE"),
                    RECEIVER_NOT_EXPORTED
                )
                Log.d("StorkyService:", "onResume_registerReceiver")
            } else {
                registerReceiver(stopwatchUpdateReceiver, IntentFilter("STOPWATCH_UPDATE"))
                Log.d("StorkyService:", "onResume_registerReceiver2")
            }
            isReceiverRegistered = true
        }

    }

    override fun onRestart() {
        Log.d("StorkyService:", "onRestart_in_Maint_activity")
        super.onRestart()

    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        Log.d("StorkyService:", "--------------------onUserLeaveHint------------------")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("StorkyService:", "onConfigurationChanged")

    }


    override fun onPause() {
        Log.d("StorkyService:", "onPause_in_Maint_activity")
        super.onPause()
    }

    override fun onStop() {
        Log.d("StorkyService:", "onStop_in_Maint_activity")
        super.onStop()
        //here uncomment:
        if (homeViewModel.isRunning.value && !homeViewModel.pauseStopWatch.value) {
            homeViewModel.startService(this)
            if (isReceiverRegistered) {
                unregisterReceiver(stopwatchUpdateReceiver)
                isReceiverRegistered = false
            }
        }
    }


    override fun onDestroy() {
        Log.d("StorkyService:", "onDestroy_in_Maint_activity")
        super.onDestroy()
        homeViewModel.stopService(this)
        if (isReceiverRegistered) {
            unregisterReceiver(stopwatchUpdateReceiver)
            isReceiverRegistered = false
        }

        /*        if (isFinishing || isChangingConfigurations) {
                    // Aktivita je ukončována uživatelem
                    Log.d("Storky ActivityLifecycle", "onDestroy - Finishing by user")
                    homeViewModel.stopService(this)
                    if (isReceiverRegistered) {
                        unregisterReceiver(stopwatchUpdateReceiver)
                        isReceiverRegistered = false
                    }

                } else if (isChangingConfigurations) {
                    // Aktivita je ukončována kvůli změně konfigurace
                    Log.d("Storky ActivityLifecycle", "onDestroy - Changing Configuration")
                } else {
                    // Aktivita je ukončována systémem (např. kvůli nedostatku paměti)
                    Log.d("Storky ActivityLifecycle", "onDestroy - System")
                }*/
    }


    private fun askPermissionPostNotification() {
        val permissionCheck = ActivityCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.POST_NOTIFICATIONS
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // User may have declined earlier, ask Android if we should show him a reason
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@MainActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                ) {
                    // show an explanation to the user...maybe TODO
                    // Good practise: don't block thread after the user sees the explanation, try again to request the permission.
                } else {
                    // request the permission.
                    // CALLBACK_NUMBER is a integer constants
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        7
                    )
                    // The callback method gets the result of the request.
                }
            }
        } else {
            // got permission use it
        }
    }


}

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun StorkyApp(intent: Intent?) {

    Surface(color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize(), content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StorkyNavigation(intent)

            }
        })

}

fun isTablet(context: Context): Boolean { //to determine if the device is a tablet
    return (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StorkyTheme {
    }
}