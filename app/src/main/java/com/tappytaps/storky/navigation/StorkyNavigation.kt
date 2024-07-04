package com.tappytaps.storky.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tappytaps.storky.screens.splash.SplashScreen
import com.tappytaps.storky.screens.bibinoapp.BibinoAppScreen
import com.tappytaps.storky.screens.email.EmailScreen
import com.tappytaps.storky.screens.email.EmailScreenViewModel
import com.tappytaps.storky.screens.history.HistoryScreen
import com.tappytaps.storky.screens.history.HistoryScreenViewModel
import com.tappytaps.storky.screens.home.HomeScreen
import com.tappytaps.storky.screens.home.HomeScreenViewModel
import com.tappytaps.storky.screens.howto.HowToScreen
import com.tappytaps.storky.screens.indicatorhelp.IndicatorHelpScreen
import com.tappytaps.storky.screens.presentation.PresentationScreen
import com.tappytaps.storky.screens.removeads.RemoveAdsScreen
import com.tappytaps.storky.screens.settings.SettingsScreen
import com.tappytaps.storky.screens.settings.SettingsScreenViewModel
import com.tappytaps.storky.screens.splash.SplashScreenViewModel
import com.tappytaps.storky.screens.trybibino.TryBibinoScreen

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun StorkyNavigation() {
    val navController = rememberNavController()

    val splashViewModel = hiltViewModel<SplashScreenViewModel>()

    val homeViewModel = hiltViewModel<HomeScreenViewModel>()
    val contractionsList =
        homeViewModel.listOfContractions.collectAsState().value //list of active Contractions

    val historyViewModel = hiltViewModel<HistoryScreenViewModel>()
    val contractionsListHistory = historyViewModel.listOfContractionsHistory.collectAsState().value

    val settingsViewModel = hiltViewModel<SettingsScreenViewModel>()
    val lengthOfInterval = settingsViewModel.lengthOfInterval.value
    val lengthOfContraction = settingsViewModel.lengthOfContraction.value

    NavHost(
        navController = navController,
        startDestination = StorkyScreens.SplashScreen.name
    ) {

        composable(StorkyScreens.SplashScreen.name) {
            SplashScreen(
                navController = navController,
                viewModel = splashViewModel
            )
        }

        composable(StorkyScreens.PresentationScreen.name) {
            PresentationScreen(navController = navController)
        }

        composable(StorkyScreens.TryBibinoScreen.name) {
            TryBibinoScreen(navController = navController)
        }

        composable(StorkyScreens.EmailScreen.name) {
            val emailViewModel = hiltViewModel<EmailScreenViewModel>()
            EmailScreen(navController = navController, viewModel = emailViewModel)
        }

        composable(StorkyScreens.HomeScreen.name) {
            HomeScreen(
                navController = navController,
                viewModel = homeViewModel,
                contractionsList = contractionsList,
                lengthOfInterval = lengthOfInterval,
                lengthOfContraction = lengthOfContraction
            )
        }

        composable(StorkyScreens.HistoryScreen.name) {
            HistoryScreen(
                navController = navController,
                viewModel = historyViewModel,
                listOfContractionsHistory = contractionsListHistory
            )
        }

        composable(StorkyScreens.IndicatorHelpScreen.name) {
            IndicatorHelpScreen(navController = navController)
        }


        composable(StorkyScreens.HowToScreen.name) {
            HowToScreen(navController = navController)
        }

        composable(StorkyScreens.SettingsScreen.name) {
            SettingsScreen(
                navController = navController,
                viewModel = settingsViewModel,
                lengthOfInterval = lengthOfInterval,
                lengthOfContraction = lengthOfContraction
            )
        }

        composable(StorkyScreens.BibinoAppScreen.name) {
            BibinoAppScreen(navController = navController)
        }


        composable(StorkyScreens.RemoveAdsScreen.name) {
            RemoveAdsScreen(navController = navController)
        }

    }

}