package com.tappytaps.android.storky.navigation

import java.lang.IllegalArgumentException

enum class StorkyScreens {
    SplashScreen,
    PresentationScreen,
    TryBibinoScreen,
    EmailScreen,
    HomeScreen,
    HistoryScreen,
    HowToScreen,
    SettingsScreen,
    RemoveAdsScreen,
    BibinoAppScreen,
    IndicatorHelpScreen;

    companion object {
        fun fromRoute(route: String?): StorkyScreens
                = when(route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            PresentationScreen.name -> PresentationScreen
            TryBibinoScreen.name -> TryBibinoScreen
            EmailScreen.name -> EmailScreen
            HomeScreen.name -> HomeScreen
            HistoryScreen.name -> HistoryScreen
            HowToScreen.name -> HowToScreen
            SettingsScreen.name -> SettingsScreen
            RemoveAdsScreen.name -> RemoveAdsScreen
            BibinoAppScreen.name -> BibinoAppScreen
            IndicatorHelpScreen.name -> IndicatorHelpScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }


}