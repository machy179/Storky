package com.tappytaps.storky.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tappytaps.storky.R
import com.tappytaps.storky.components.StorkyAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenSecondPage(
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    viewModel: SettingsScreenViewModel,
    lengthOfInterval: Int,
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StorkyAppBar(
                titleNameOfScreen = stringResource(R.string.length_of_interval),
                backgroundColor = MaterialTheme.colorScheme.background,
                closeIconVisible = true,
                deleteIconVisible = false,
                onClose = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { paddingValues ->
        val verticalPaddingOfRows = 8.dp
        val verticalPaddingOfDivider = 0.dp
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues)
                    .padding(horizontal = 16.dp)
            ) {

                HorizontalDivider(modifier = Modifier.padding(vertical = verticalPaddingOfDivider))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = verticalPaddingOfRows)
                        .clickable {
                            viewModel.setLengthOfInterval(240)
                            coroutineScope.launch {

                                pagerState.animateScrollToPage(0)
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.minutes_4),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start,
                    )
                    if (lengthOfInterval == 240) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Center Icon",
                            modifier = Modifier,
                            tint = MaterialTheme.colorScheme.primary,

                            )
                    }

                }
                HorizontalDivider(modifier = Modifier.padding(vertical = verticalPaddingOfDivider))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = verticalPaddingOfRows)
                        .clickable {
                            viewModel.setLengthOfInterval(270)
                            coroutineScope.launch {

                                pagerState.animateScrollToPage(0)

                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.minutes_430),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start,
                    )
                    if (lengthOfInterval == 270) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Center Icon",
                            modifier = Modifier,
                            tint = MaterialTheme.colorScheme.primary,

                            )
                    }


                }
                HorizontalDivider(modifier = Modifier.padding(vertical = verticalPaddingOfDivider))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = verticalPaddingOfRows)
                        .clickable {
                            viewModel.setLengthOfInterval(300)
                            coroutineScope.launch {

                                pagerState.animateScrollToPage(0)

                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.minutes_5),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start,
                    )
                    if (lengthOfInterval == 300) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Center Icon",
                            modifier = Modifier,
                            tint = MaterialTheme.colorScheme.primary,

                            )
                    }

                }
                HorizontalDivider(modifier = Modifier.padding(vertical = verticalPaddingOfDivider))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = verticalPaddingOfRows)
                        .clickable {
                            //     chooseOfLengthTime = 330
                            viewModel.setLengthOfInterval(330)
                            coroutineScope.launch {

                                pagerState.animateScrollToPage(0)
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.minutes_530),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start,
                    )
                    if (lengthOfInterval == 330) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Center Icon",
                            modifier = Modifier,
                            tint = MaterialTheme.colorScheme.primary,

                            )
                    }

                }
                HorizontalDivider(modifier = Modifier.padding(vertical = verticalPaddingOfDivider))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = verticalPaddingOfRows)
                        .clickable {
                            //    chooseOfLengthTime = 360
                            viewModel.setLengthOfInterval(360)
                            coroutineScope.launch {

                                pagerState.animateScrollToPage(0)
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.minutes_6),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start,
                    )
                    if (lengthOfInterval == 360) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            //     painter = painterResource(id = ),
                            contentDescription = "Center Icon",
                            modifier = Modifier,
                            tint = MaterialTheme.colorScheme.primary,

                            )
                    }


                }
                HorizontalDivider(modifier = Modifier.padding(vertical = verticalPaddingOfDivider))

            }


        }


    }

}

@Preview
@Composable
fun SettingScreenSecondPagePreview() {
    SettingsScreenSecondPage(
        pagerState = rememberPagerState(pageCount = {
            3
        }),
        coroutineScope = rememberCoroutineScope(),
        viewModel = hiltViewModel<SettingsScreenViewModel>(),
        lengthOfInterval = 300
    )
}