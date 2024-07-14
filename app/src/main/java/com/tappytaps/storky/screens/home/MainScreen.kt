package com.tappytaps.storky.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tappytaps.storky.R
import com.tappytaps.storky.components.ContractionRow
import com.tappytaps.storky.components.ContractionRowByItems
import com.tappytaps.storky.components.CustomDialog
import com.tappytaps.storky.components.MainScreenAppBar
import com.tappytaps.storky.components.UniversalButton
import com.tappytaps.storky.components.ImageTitleContentText
import com.tappytaps.storky.model.Contraction
import com.tappytaps.storky.navigation.StorkyScreens
import com.tappytaps.storky.utils.convertSecondsToTimeString
import com.tappytaps.storky.utils.convertSecondsToTimeString2


@Composable
fun MainScreen(
    navController: NavController,
    contractionsList: List<Contraction>,
    viewModel: HomeScreenViewModel,
    lengthOfInterval: Int,
    lengthOfContraction: Int,
) {

    val currentContractionLength = viewModel.currentContractionLength.value
    val currentTimeDateContraction = viewModel.currentTimeDateContraction.value
    val currentLengthBetweenContractions = viewModel.currentLengthBetweenContractions.value

    val pauseStopWatch = viewModel.pauseStopWatch.value
    var dialogNewMonitoringVisible by remember { mutableStateOf(false) }

    val averageContractionLength = viewModel.averageContractionLength.value
    val averageLengthBetweenContractions = viewModel.averageLengthBetweenContractions.value

    val dialogShownAutomatically = viewModel.dialogShownAutomatically
    var showDialogAutomatically by remember { mutableStateOf(false) }

    //check if is it possible to show StorkyPopUpDialog automatically:
    LaunchedEffect(
        lengthOfInterval,
        lengthOfContraction,
        averageContractionLength,
        averageLengthBetweenContractions
    ) {
        if ((averageContractionLength > lengthOfContraction) && (averageLengthBetweenContractions < lengthOfInterval) && (averageLengthBetweenContractions > 0) && !dialogShownAutomatically.value) {
            showDialogAutomatically = true

        }
    }


    Scaffold(
        topBar = {
            MainScreenAppBar(
                intervalContractionTextSetting = convertSecondsToTimeString2(lengthOfContraction),
                intervalBetweenTextSetting = convertSecondsToTimeString2(lengthOfInterval),
                navController = navController,
                backgroundColor = MaterialTheme.colorScheme.background,
                pauseIconVisible = !pauseStopWatch,
                intervalContractionTextCurrent = if (averageContractionLength != 0) convertSecondsToTimeString2(
                    averageContractionLength
                ) else "-:--",
                intervalBetweenTextCurrent = if (averageLengthBetweenContractions != 0) convertSecondsToTimeString2(
                    averageLengthBetweenContractions
                ) else "-:--",
                onPause = {
                    viewModel.pauseStopWatch()
                },
                onHistory = {
                    navController.navigate(StorkyScreens.HistoryScreen.name)
                },
                onNewMonitoring = {
                    dialogNewMonitoringVisible = true
                },
                onIndicatorActive = {
                    //     viewModel.updateAverageTimes()
                },
                contractionsOk = averageContractionLength > lengthOfContraction,
                intervalsOk = (averageLengthBetweenContractions < lengthOfInterval) && (averageLengthBetweenContractions != 0),
                showDialogAutomatically = showDialogAutomatically,
                onDismisStorkyPopUpDialog = {
                    if (showDialogAutomatically) {
                        viewModel.setDialogShownAutomaticallyTrue()
                        showDialogAutomatically = false
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    //   horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                        .graphicsLayer { alpha = 0.99F }
                        .drawWithContent {
                            val colors = listOf(Color.Black, Color.Transparent)
                            drawContent()

                            // Calculate the starting and ending position of the gradient
                            val gradientStartY = size.height * 0.75f
                            val gradientEndY = size.height * 0.95f

                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors,
                                    startY = gradientStartY,
                                    endY = gradientEndY
                                ),
                                blendMode = BlendMode.DstIn
                            )
                        }
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        if (contractionsList.isNullOrEmpty() && !viewModel.isRunning) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                ImageTitleContentText(
                                    imageResId = R.drawable.girl_ball,
                                    titleResId = R.string.practical_knowledge_title,
                                    textResId = R.string.practical_knowledge_text,
                                    learnMore = true,
                                    navController = navController
                                )
                            }

                        } else {
                            if (viewModel.isRunning) { //because if is first open, countdowner still does not work - so nothing to display on top text area
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    if (pauseStopWatch) {
                                        Text(
                                            text = stringResource(R.string.paused_tap_start),
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier
                                                .width(213.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    } else {
                                        Text(
                                            text = convertSecondsToTimeString(
                                                currentLengthBetweenContractions
                                            ),
                                            style = MaterialTheme.typography.displayMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            textAlign = TextAlign.Center
                                            //     fontSize = 48.sp
                                        )
                                    }


                                    Text(
                                        text = if (!pauseStopWatch) stringResource(R.string.interval_time) else "",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )
                                }



                                ContractionRow(
                                    lengthOfContraction = currentContractionLength,
                                    contractionTime = currentTimeDateContraction,
                                    timeBetweenContractions = currentLengthBetweenContractions,
                                    numberOfContraction = contractionsList.size + 1
                                )
                            }

                            val listState = rememberLazyListState()
                            LaunchedEffect(key1 = Unit) {
                                listState.animateScrollToItem(index = 0) //it is for roll lazycolumn to top
                            }


                            LazyColumn(
                                modifier = Modifier,
                                verticalArrangement = Arrangement.Top, // Start items from the top
                                state = listState,

                                ) {
                                var reverseIndex = contractionsList.size
                                itemsIndexed(contractionsList) { index, contraction ->
                                    reverseIndex = contractionsList.size - index
                                    ContractionRowByItems(contraction = contraction,
                                        numberOfContraction = reverseIndex,
                                        deleteContraction = {
                                            viewModel.deleteContraction(
                                                contraction
                                            )
                                        })
                                }

                            }
                        }


                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    UniversalButton(
                        text = stringResource(R.string.start_contraction),
                        onClick = {
                            // Button click logic
                            viewModel.saveContraction()
                            viewModel.updateCurrentTime()
                            viewModel.newStart()
                            viewModel.setShowContractionlScreen(value = true)

                        },
                        disableInsetNavigationBarPadding = true
                    )
                }

                if (dialogNewMonitoringVisible) {
                    CustomDialog(
                        title = stringResource(R.string.new_monitoring_title),
                        text = stringResource(R.string.new_monitoring_text),
                        firstTextButton = stringResource(R.string.no),
                        secondTextButton = stringResource(R.string.ok),
                        enableFirstRequest = true,
                        firstRequest = {
                            viewModel.newMonitoring()
                            dialogNewMonitoringVisible = false
                        },
                        onDismissRequest = { dialogNewMonitoringVisible = false },
                        changePositionButtons = true
                    )
                }
            }

        }

    }
}