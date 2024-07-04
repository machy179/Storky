package com.tappytaps.storky.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tappytaps.storky.R
import com.tappytaps.storky.navigation.StorkyScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, viewModel: SplashScreenViewModel) {

    val isFirstRun: Boolean by remember { mutableStateOf(viewModel.checkFirstRun()) }

    if (isFirstRun) {
        logo()
        LaunchedEffect(key1 = true) {
            delay(1000L)
            navController.navigate(StorkyScreens.PresentationScreen.name)

        }
    } else {
        //      navController.navigate(StorkyScreens.HomeScreen.name)
        LaunchedEffect(key1 = true) {
            delay(200L)
            navController.navigate(StorkyScreens.HomeScreen.name)
        }

    }


}


@Preview
@Composable
private fun logo() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .width(280.dp)
                .align(Alignment.Center),
            //     .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Center images horizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Your Splash Screen Image",
                modifier = Modifier
            )

        }
    }
}
