package com.tappytaps.storky.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.ui.Alignment

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
                        pagerState.scrollToPage(0)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { paddingValues ->
        val verticalPaddingOfRows = 0.dp
        val verticalPaddingOfDivider = 0.dp
        val coroutineScope = rememberCoroutineScope()

        val intervals = listOf(
            240 to R.string.minutes_4,
            270 to R.string.minutes_430,
            300 to R.string.minutes_5,
            330 to R.string.minutes_530,
            360 to R.string.minutes_6
        )

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                intervals.forEach { interval ->
                    val (value, labelRes) = interval
                    HorizontalDivider(modifier = Modifier.padding(vertical = verticalPaddingOfDivider))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = verticalPaddingOfRows)
                            .clickable {
                                viewModel.setLengthOfInterval(value)
                                coroutineScope.launch {
                                    pagerState.scrollToPage(0)
                                }
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(labelRes),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start
                        )
                        RadioButton(
                            selected = lengthOfInterval == value,
                            onClick = {
                                viewModel.setLengthOfInterval(value)
                                coroutineScope.launch {
                                    pagerState.scrollToPage(0)
                                }
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
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