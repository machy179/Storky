package com.tappytaps.android.storky.screens.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.tappytaps.android.storky.R
import com.tappytaps.android.storky.components.ImageTitleContentText
import com.tappytaps.android.storky.navigation.StorkyScreens
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape


@ExperimentalFoundationApi
@Composable
fun PresentationScreen(navController: NavController) {
    HorizontalPagerWithButtonsScreen(navController)
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@ExperimentalFoundationApi
@Composable
fun HorizontalPagerWithButtonsScreen(navController: NavController) {
    val pageCount = 3
    val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = {
        3
    })
    val coroutineScope = rememberCoroutineScope()

    val shouldShowPrevButton by remember {
        derivedStateOf { pagerState.currentPage > 0 }
    }

    val shouldShowNextButton by remember {
        derivedStateOf { pagerState.currentPage < pageCount - 1 }
    }
    val context = LocalContext.current

    // Launcher to request permissions
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Handle permission result here if needed
    }



    Column(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars) //for bottom padding shifted under navigation bar
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f).padding(top = 64.dp)

        ) { page ->

            ContentPage(page = page)
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // Previous Button - hidden
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
                    text = "", // Assuming you have a string for this
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            // Indicator Dots
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
                    }
                    Box(
                        modifier = Modifier
                            .padding(4.dp) // Padding between dots
                            .size(8.dp)
                            .background(color, shape = CircleShape)
                    )
                }
            }

            // Next Button
            TextButton(
                onClick = {
                    if (shouldShowNextButton) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        // Navigate to the next screen
                        navController.navigate(StorkyScreens.TryBibinoScreen.name)

                        // Check for permission here
                        checkPermissionPostNotification(context, launcher)
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
fun ContentPage(
    page: Int,
) {
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
                textResId = textResId,
                modifier = Modifier.fillMaxWidth(),
                bottomSpace = false

            )


        }
    }

}

//function to check and set permission
private fun checkPermissionPostNotification(
    context: Context,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
) {
    // Check initial permission state
    val permissionCheck = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
    )

    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Launch permission request
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
private fun HorizontalPagerWithButtonsScreenPreview() {
    HorizontalPagerWithButtonsScreen(NavController(LocalContext.current))
}