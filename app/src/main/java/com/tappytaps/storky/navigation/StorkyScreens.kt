package com.tappytaps.storky.navigation

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
    ShareEmailSenderScreen,
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
            ShareEmailSenderScreen.name -> ShareEmailSenderScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }


}