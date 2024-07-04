package com.tappytaps.storky.screens.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.tappytaps.storky.R
import com.tappytaps.storky.components.ImageTitleContentText
import com.tappytaps.storky.navigation.StorkyScreens
import kotlinx.coroutines.launch
import com.google.accompanist.pager.*

@ExperimentalFoundationApi
@Composable
fun PresentationScreen(navController: NavController) {
    HorizontalPagerWithButtonsScreen(navController)
}


@ExperimentalFoundationApi
@Composable
fun HorizontalPagerWithButtonsScreen(navController: NavController) {
    val pageCount = 3
//    val pagerState = rememberPagerState(pageCount = { pageCount })
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val shouldShowPrevButton by remember {
        derivedStateOf { pagerState.currentPage > 0 }
    }

    val shouldShowNextButton by remember {
        derivedStateOf { pagerState.currentPage < pageCount - 1 }
    }

    Column(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars) //for bottom padding shifted under navigation bar
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HorizontalPager(
            count = 3,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            ContentPage(page)
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp), //bottom = 16.dp
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            TextButton(
                onClick = {
                    if (shouldShowPrevButton) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                },
                enabled = shouldShowPrevButton,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.back_button),
                    color = if (shouldShowPrevButton) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    style = MaterialTheme.typography.labelLarge
                )
            }



            HorizontalPagerIndicator( //TODO - update, this is deprecated
                pagerState = pagerState,
                modifier = Modifier
                    //        .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                activeColor = MaterialTheme.colorScheme.primary,
                inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant


            )


            TextButton(
                onClick = {
                    if (shouldShowNextButton) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        navController.navigate(StorkyScreens.TryBibinoScreen.name)
                    }
                },
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.next_button),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }

        }

    }

}


@Composable
fun ContentPage(page: Int) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            //            .padding(start = 40.dp, end = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Center images horizontally
        ) {

            var imageResId: Int = R.drawable.welcome
            var titleResId: Int = R.string.welcome_to_storky
            var textResId: Int = R.string.presentation_text_1
            when (page) {
                0 -> {
                    imageResId = R.drawable.welcome
                    titleResId = R.string.welcome_to_storky
                    textResId = R.string.presentation_text_1
                }

                1 -> {
                    imageResId = R.drawable.measure
                    titleResId = R.string.simple_measuring
                    textResId = R.string.presentation_text_2
                }

                2 -> {
                    imageResId = R.drawable.tips
                    titleResId = R.string.smart_notification
                    textResId = R.string.presentation_text_3
                }

            }

            ImageTitleContentText(
                imageResId = imageResId,
                titleResId = titleResId,
                textResId = textResId
            )


        }
    }

}

@ExperimentalFoundationApi
@Preview
@Composable
private fun HorizontalPagerWithButtonsScreenPreview() {
    HorizontalPagerWithButtonsScreen(NavController(LocalContext.current))
}