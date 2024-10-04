package com.tappytaps.android.storky

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import com.google.android.gms.ads.MobileAds
import com.tappytaps.android.storky.navigation.StorkyNavigation
import com.tappytaps.android.storky.screens.home.HomeScreenViewModel
import com.tappytaps.android.storky.ui.theme.StorkyTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeScreenViewModel by viewModels()

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

    }


    override fun onResume() {
        super.onResume()
        homeViewModel.checkIfItIsFromService()
        homeViewModel.stopService(this)

    }

    override fun onStop() {
        super.onStop()
        if (homeViewModel.isRunning.value && !homeViewModel.pauseStopWatch.value) {
            homeViewModel.startService(this)
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