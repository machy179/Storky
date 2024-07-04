package com.tappytaps.storky.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.tappytaps.storky.R
import com.tappytaps.storky.components.StorkyAppBar
import com.tappytaps.storky.components.UniversalButton
import com.tappytaps.storky.utils.convertSecondsToTimeString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractionScreen(
    viewModel: HomeScreenViewModel,
) {
    val currentLengthBetweenContractions = viewModel.currentLengthBetweenContractions.value


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
        Surface(
            modifier = Modifier
                .padding(paddingValues)
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
                        Text(
                            text = convertSecondsToTimeString(currentLengthBetweenContractions),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    UniversalButton(
                        text = stringResource(R.string.stop_contraction),
                        onClick = {
                            viewModel.setShowContractionlScreen(value = false)
                            viewModel.saveCurrentContractionLength()
                            viewModel.updateAverageTimes()
                            viewModel.setButtonStopContractionAlreadyPresed(value = true)
                        },
                        inverseColor = true,
                        disableInsetNavigationBarPadding = true
                    )
                }
            }
        }

    }
}