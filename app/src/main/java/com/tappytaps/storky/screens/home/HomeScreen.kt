package com.tappytaps.storky.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tappytaps.storky.model.Contraction
import com.tappytaps.storky.ui.theme.ChangeStatusBarTextColor

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
) {
    val showContractionlScreen = viewModel.showContractionlScreen

    if (!showContractionlScreen.value) {
        ChangeStatusBarTextColor(false)
        MainScreen(
            navController = navController,
            contractionsList = contractionsList,
            viewModel = viewModel,
            lengthOfInterval = lengthOfInterval,
            lengthOfContraction = lengthOfContraction
        )
    } else {
        ChangeStatusBarTextColor(true)
        ContractionScreen(
            viewModel = viewModel
        )
    }



}







