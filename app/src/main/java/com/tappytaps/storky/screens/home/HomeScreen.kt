package com.tappytaps.storky.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tappytaps.storky.model.Contraction

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    contractionsList: List<Contraction>,
    lengthOfInterval: Int,
    lengthOfContraction: Int
) {
    val showContractionlScreen = viewModel.showContractionlScreen
 //       remember { mutableStateOf(false) } //is used for changing icons on topbar and button color too

    if (!showContractionlScreen.value) {
        MainScreen(
            navController = navController,
            showContractionlScreen = showContractionlScreen,
            contractionsList = contractionsList,
            viewModel = viewModel,
            lengthOfInterval = lengthOfInterval,
            lengthOfContraction = lengthOfContraction
        )
    } else {
        ContractionScreen(
            navController = navController,
            showContractionlScreen = showContractionlScreen,
            viewModel = viewModel
        )
    }


}







