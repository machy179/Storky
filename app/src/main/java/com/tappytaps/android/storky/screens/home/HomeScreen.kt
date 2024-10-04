package com.tappytaps.android.storky.screens.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.window.layout.WindowMetricsCalculator
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.tappytaps.android.storky.BuildConfig
import com.tappytaps.android.storky.model.Contraction
import com.tappytaps.android.storky.ui.theme.ChangeStatusBarTextColor
import com.tappytaps.android.storky.utils.Constants

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    contractionsList: List<Contraction>,
    lengthOfInterval: Int,
    lengthOfContraction: Int,
    adsDisabled: Boolean,
) {
    var paddingValuesState by remember { mutableStateOf(PaddingValues()) } // State to hold paddingValues
    var adHeight by remember { mutableStateOf(0) } // State to store ad height in dp

    //check if user bought the app in current session - so adHeight is setting to 0
    LaunchedEffect(
        adsDisabled
    ) {
        if (adsDisabled == true) adHeight = 0
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val showContractionlScreen = viewModel.showContractionlScreen
        if (!showContractionlScreen.value) {
            ChangeStatusBarTextColor(false)
            MainScreen(
                navController = navController,
                contractionsList = contractionsList,
                viewModel = viewModel,
                lengthOfInterval = lengthOfInterval,
                lengthOfContraction = lengthOfContraction,
                onPaddingValuesChanged = { paddingValues -> // Receive paddingValues from ContractionScreen
                    paddingValuesState = paddingValues
                },
                bottomPadding = adHeight,
                adsDisabled = adsDisabled

            )
        } else {
            ChangeStatusBarTextColor(true)
            ContractionScreen(
                viewModel = viewModel,
                onPaddingValuesChanged = { paddingValues -> // Receive paddingValues from ContractionScreen
                    paddingValuesState = paddingValues
                },
                bottomPadding = adHeight
            )
        }
        if (!adsDisabled) {


            Column(
                modifier = Modifier
                    .fillMaxSize().padding(bottom = paddingValuesState.calculateBottomPadding())
                    .align(Alignment.BottomCenter),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                AdaptiveBannerAd { heightInDp ->
                    adHeight = heightInDp // Update ad height when calculated
                    Log.d("Adaptive banner height:", adHeight.toString())
                }
            }
        }

    }


}

@Composable
fun AdaptiveBannerAd(onAdHeightCalculated: (Int) -> Unit) {
    val context = LocalContext.current
    val adView = remember { AdView(context) }

    val adSize = remember {
        calculateAdSize(context)
    }

    // Determine the ad unit ID based on whether the build is debug or release
    val adUnitIdBanner =
        if (BuildConfig.DEBUG) Constants.AD_UNIT_ID_BANNER_TEST else Constants.AD_UNIT_ID_BANNER_TAPPYTAPS


    // Apply only if adUnitId has not been set yet - because of crasch during change Dark/Light mode
    adView.apply {
        if (adUnitId.isNullOrEmpty()) {
            adUnitId = adUnitIdBanner
            setAdSize(adSize)
        }
    }

    val adRequest = remember { AdRequest.Builder().build() }

    // Get the current density
    val density = LocalDensity.current

    // Report the calculated ad height using the onAdHeightCalculated callback
    LaunchedEffect(adSize) {
        with(density) {
            onAdHeightCalculated(adSize.height)
        }
    }

    AndroidView(
        factory = {
            adView.apply {
                loadAd(adRequest)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}

fun calculateAdSize(context: Context): AdSize {
    val metrics = WindowMetricsCalculator.getOrCreate()
        .computeCurrentWindowMetrics(context as Activity)

    val density = context.resources.displayMetrics.density
    val adWidthPixels = metrics.bounds.width().toFloat()
    val adWidth = (adWidthPixels / density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
}







