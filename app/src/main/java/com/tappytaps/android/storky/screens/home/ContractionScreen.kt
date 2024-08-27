package com.tappytaps.android.storky.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tappytaps.android.storky.R
import com.tappytaps.android.storky.components.StorkyAppBar
import com.tappytaps.android.storky.components.UniversalButton
import com.tappytaps.android.storky.utils.convertSecondsToTimeString
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieConstants



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractionScreen(
    viewModel: HomeScreenViewModel,
    onPaddingValuesChanged: (PaddingValues) -> Unit,
    bottomPadding: Int
) {
    val currentLengthBetweenContractions = viewModel.currentLengthBetweenContractions.value

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.storky_circle)
    )

    Scaffold(
        topBar = {
            StorkyAppBar(
                backgroundColor = MaterialTheme.colorScheme.primary,
                onDelete = {
                    viewModel.deleteCurrentContraction()
                    viewModel.setShowContractionlScreen(value = false)
                    viewModel.updateAverageTimes()
                }

            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { paddingValues ->
        onPaddingValuesChanged(paddingValues) // Pass paddingValues back to HomeScreen
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .padding(bottom = bottomPadding.dp)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LottieAnimation(
                                composition = composition,
                                iterations = LottieConstants.IterateForever
                            )
                            Text(
                                text = convertSecondsToTimeString(currentLengthBetweenContractions),
                                style = MaterialTheme.typography.displayLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column() {
                            UniversalButton(
                                text = stringResource(R.string.stop_contraction),
                                onClick = {
                                    viewModel.setShowContractionlScreen(value = false)
                                    viewModel.saveCurrentContractionLength()
                                    viewModel.updateAverageTimes()
                                    viewModel.setButtonStopContractionAlreadyPresed(value = true)
                                    viewModel.setAlarmAfter5Days()
                                },
                                inverseColor = true,
                                disableInsetNavigationBarPadding = true,
                                sendButton = true
                            )
                        }

                    }
                }
            }

        }
    }
}
