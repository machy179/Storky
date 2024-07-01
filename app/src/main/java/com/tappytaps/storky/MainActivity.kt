package com.tappytaps.storky

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //      enableEdgeToEdge()

        enableEdgeToEdge(
            /*            statusBarStyle = SystemBarStyle.dark(
                            scrim =  android.graphics.Color.TRANSPARENT,
                        ),*/
            navigationBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )


        setContent {
            StorkyTheme {
                StorkyApp()
            }


        }

        askPermissionPostNotification()


        stopwatchUpdateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val currentLengthBetweenContractions =
                    intent?.getIntExtra("currentLengthBetweenContractions", 0) ?: 0
                val pauseStopWatch = intent?.getBooleanExtra("pauseStopWatch", false) ?: false
                val showContractionlScreen = intent?.getBooleanExtra("showContractionlScreen", false) ?: false
                homeViewModel.updateFromService(currentLengthBetweenContractions, pauseStopWatch, showContractionlScreen)
            }
        }
 //       registerReceiver(stopwatchUpdateReceiver, IntentFilter("STOPWATCH_UPDATE"))
    }

    override fun onResume() {
        Log.d("StorkyService:","onResume_in_Maint_activity")
        super.onResume()
        homeViewModel.stopService(this)
        registerReceiver(stopwatchUpdateReceiver, IntentFilter("STOPWATCH_UPDATE"))

    }

    override fun onRestart() {
        Log.d("StorkyService:","onRestart_in_Maint_activity")
        super.onRestart()

    }


  override fun onStop() {
        Log.d("StorkyService:","onPause_in_Maint_activity")
      super.onStop()
      if(homeViewModel.isRunning && !homeViewModel.pauseStopWatch.value) {
          homeViewModel.startService(this)
          unregisterReceiver(stopwatchUpdateReceiver)
      }
    }

    override fun onDestroy() {
        Log.d("StorkyService:","onDestroy_in_Maint_activity")
        super.onDestroy()
        homeViewModel.stopService(this)
        unregisterReceiver(stopwatchUpdateReceiver)
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
fun StorkyApp() {

    Surface(color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(), content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StorkyNavigation()

            }
        })

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StorkyTheme {
    }
}