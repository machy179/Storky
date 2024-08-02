package com.tappytaps.storky.screens.settings

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsScreenViewModel,
    lengthOfInterval: Int,
    lengthOfContraction: Int,
) {
    val pagerState = rememberPagerState(pageCount = {
        3
    })

    val coroutineScope = rememberCoroutineScope()



    HorizontalPager(
        userScrollEnabled = false,
        state = pagerState,
        modifier = Modifier.fillMaxSize()

    ) { page ->
        when (page) {
            0 -> SettingsScreenFirstPage(
                navController = navController,
                pagerState = pagerState,
                coroutineScope = coroutineScope,
                viewModel = viewModel,
                lengthOfInterval = lengthOfInterval,
                lengthOfContraction = lengthOfContraction
            )

            1 -> SettingsScreenSecondPage(
                pagerState = pagerState,
                coroutineScope = coroutineScope,
                viewModel = viewModel,
                lengthOfInterval = lengthOfInterval
            )

            2 -> SettingsScreenThirdPage(
                pagerState = pagerState,
                coroutineScope = coroutineScope,
                viewModel = viewModel,
                lengthOfContraction = lengthOfContraction
            )
        }
    }
}

