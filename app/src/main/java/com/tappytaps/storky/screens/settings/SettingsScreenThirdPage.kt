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


@ExperimentalMaterial3Api
@Composable
fun SettingsScreenThirdPage(
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    viewModel: SettingsScreenViewModel,
    lengthOfContraction: Int
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
                modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues).padding(horizontal = 16.dp)
            ) {

                HorizontalDivider(modifier = Modifier.padding(vertical=verticalPaddingOfDivider))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = verticalPaddingOfRows)
                        .clickable {
                            viewModel.setLengthOfContraction(50)
                            coroutineScope.launch {

                                pagerState.animateScrollToPage(0)
                            } },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.seconds_50),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start,
                    )
                    if(lengthOfContraction == 50) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            //     painter = painterResource(id = ),
                            contentDescription = "Center Icon",
                            modifier = Modifier,
                            tint = MaterialTheme.colorScheme.primary,

                            )
                    }

                }
                HorizontalDivider(modifier = Modifier.padding(vertical=verticalPaddingOfDivider))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = verticalPaddingOfRows).clickable {
                        //            chooseOfLengthTime = 270
                        viewModel.setLengthOfContraction(60)
                        coroutineScope.launch {

                            pagerState.animateScrollToPage(0)

                        } },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.minute_1),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start,
                    )
                    if(lengthOfContraction  == 60) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            //     painter = painterResource(id = ),
                            contentDescription = "Center Icon",
                            modifier = Modifier,
                            tint = MaterialTheme.colorScheme.primary,

                            )
                    }


                }
                HorizontalDivider(modifier = Modifier.padding(vertical=verticalPaddingOfDivider))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = verticalPaddingOfRows).clickable {
                        //          chooseOfLengthTime = 300
                        viewModel.setLengthOfContraction(70)
                        coroutineScope.launch {

                            pagerState.animateScrollToPage(0)

                        } },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.minute_110),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start,
                    )
                    if(lengthOfContraction  == 70) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            //     painter = painterResource(id = ),
                            contentDescription = "Center Icon",
                            modifier = Modifier,
                            tint = MaterialTheme.colorScheme.primary,

                            )
                    }

                }
                HorizontalDivider(modifier = Modifier.padding(vertical=verticalPaddingOfDivider))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = verticalPaddingOfRows).clickable {
                        //     chooseOfLengthTime = 330
                        viewModel.setLengthOfContraction(80)
                        coroutineScope.launch {

                            pagerState.animateScrollToPage(0)
                        } },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.minute_120),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start,
                    )
                    if(lengthOfContraction  == 80) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            //     painter = painterResource(id = ),
                            contentDescription = "Center Icon",
                            modifier = Modifier,
                            tint = MaterialTheme.colorScheme.primary,

                            )
                    }

                }
                HorizontalDivider(modifier = Modifier.padding(vertical=verticalPaddingOfDivider))

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