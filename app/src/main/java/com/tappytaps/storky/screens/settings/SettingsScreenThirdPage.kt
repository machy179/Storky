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


@ExperimentalMaterial3Api
@Composable
fun SettingsScreenThirdPage(
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    viewModel: SettingsScreenViewModel,
    lengthOfContraction: Int,
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

        val contractionLengths = listOf(
            50 to R.string.seconds_50,
            60 to R.string.minute_1,
            70 to R.string.minute_110,
            80 to R.string.minute_120
        )


        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(verticalPaddingOfRows)
            ) {
                contractionLengths.forEach { contractionLength ->
                    val (value, labelRes) = contractionLength
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = verticalPaddingOfRows)
                            .clickable {
                                viewModel.setLengthOfContraction(value)
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
                            selected = lengthOfContraction == value,
                            onClick = {
                                viewModel.setLengthOfContraction(value)
                                coroutineScope.launch {
                                    pagerState.scrollToPage(0)
                                }
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                        )
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = verticalPaddingOfDivider))
                }
            }
        }

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SettingsScreenThirdPagePreview() {
    SettingsScreenThirdPage(
        pagerState = rememberPagerState(pageCount = {
            3
        }),
        coroutineScope = rememberCoroutineScope(),
        viewModel = hiltViewModel<SettingsScreenViewModel>(),
        lengthOfContraction = 60
    )
}