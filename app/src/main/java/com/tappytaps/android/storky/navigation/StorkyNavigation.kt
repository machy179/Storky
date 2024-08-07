package com.tappytaps.android.storky.navigation

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tappytaps.android.storky.screens.splash.SplashScreen
import com.tappytaps.android.storky.screens.bibinoapp.BibinoAppScreen
import com.tappytaps.android.storky.screens.email.EmailScreen
import com.tappytaps.android.storky.screens.email.EmailScreenViewModel
import com.tappytaps.android.storky.screens.history.HistoryScreen
import com.tappytaps.android.storky.screens.history.HistoryScreenViewModel
import com.tappytaps.android.storky.screens.home.HomeScreen
import com.tappytaps.android.storky.screens.home.HomeScreenViewModel
import com.tappytaps.android.storky.screens.howto.HowToScreen
import com.tappytaps.android.storky.screens.indicatorhelp.IndicatorHelpScreen
import com.tappytaps.android.storky.screens.presentation.PresentationScreen
import com.tappytaps.android.storky.screens.removeads.RemoveAdsScreen
import com.tappytaps.android.storky.screens.removeads.RemoveAdsScreenViewModel
import com.tappytaps.android.storky.screens.settings.SettingsScreen
import com.tappytaps.android.storky.screens.settings.SettingsScreenViewModel
import com.tappytaps.android.storky.screens.splash.SplashScreenViewModel
import com.tappytaps.android.storky.screens.trybibino.TryBibinoScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun StorkyNavigation(intent: Intent?) {
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

    val removeAdsViewModel = hiltViewModel<RemoveAdsScreenViewModel>()
    val adsDisabled by removeAdsViewModel.adsDisabled.collectAsState()

    val currentScreen = rememberSaveable { mutableStateOf(StorkyScreens.SplashScreen.name) }

    LaunchedEffect(intent) { // because of click on notification after 5 days, the app is running and skip to TryBibinoScreen
        intent?.getStringExtra("screen")?.let { screen ->
            if (screen == "TryBibinoScreen") {
                navController.navigate(StorkyScreens.TryBibinoScreen.name)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = currentScreen.value
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
                lengthOfContraction = lengthOfContraction,
                adsDisabled = adsDisabled
            )
        }

        animatedComposable(
            StorkyScreens.HistoryScreen.name,
            navController = navController) {
            HistoryScreen(
                navController = it,
                historyViewModel = historyViewModel,
                listOfContractionsHistory = contractionsListHistory,
                homeViewModel = homeViewModel,
                listOfActiveContractions = contractionsList,
                adsDisabled = adsDisabled
            )
        }

        animatedComposable(
            StorkyScreens.IndicatorHelpScreen.name,
            navController = navController) {
            IndicatorHelpScreen(navController = it)
        }


        animatedComposable(
            StorkyScreens.HowToScreen.name,
            navController = navController) {
            HowToScreen(navController = it)
        }

        animatedComposable(
            StorkyScreens.SettingsScreen.name,
            navController = navController) {
            SettingsScreen(
                navController = it,
                viewModel = settingsViewModel,
                lengthOfInterval = lengthOfInterval,
                lengthOfContraction = lengthOfContraction
            )
        }


        animatedComposable(
            route = StorkyScreens.BibinoAppScreen.name,
            navController = navController
        ) {
            BibinoAppScreen(navController = it)
        }

        animatedComposable(
            route = StorkyScreens.RemoveAdsScreen.name,
            navController = navController
        ) {
            RemoveAdsScreen(navController = it, viewModel = removeAdsViewModel)
        }



    }

}

//because of animation effect
fun NavGraphBuilder.animatedComposable(
    route: String,
    navController: NavHostController,
    content: @Composable (NavHostController) -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up, tween(600)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down, tween(600)
            )
        }
    ) {
        content(navController)
    }
}