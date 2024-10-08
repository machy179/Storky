package com.tappytaps.android.storky.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tappytaps.android.storky.R
import com.tappytaps.android.storky.components.StorkyAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.tappytaps.android.storky.navigation.StorkyScreens
import com.tappytaps.android.storky.utils.Constants
import com.tappytaps.android.storky.utils.convertSecondsToStringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenFirstPage(
    navController: NavController,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    viewModel: SettingsScreenViewModel,
    lengthOfInterval: Int,
    lengthOfContraction: Int,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StorkyAppBar(
                titleNameOfScreen = stringResource(R.string.settings),
                backgroundColor = MaterialTheme.colorScheme.background,
                closeIconVisible = true,
                deleteIconVisible = false,
                onClose = {
                    navController.navigate(StorkyScreens.HomeScreen.name)
                },
                scrollBehavior = null
            )
        },
    ) { paddingValues ->

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState) // Add vertical scroll
                    .padding(top = paddingValues.calculateTopPadding())

            ) {
                Spacer(modifier = Modifier.height(height = 16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, // Space out items to opposite ends
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = stringResource(R.string.storky_indicator),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start
                    )

                    IconButton(
                        onClick = {
                            navController.navigate(StorkyScreens.IndicatorHelpScreen.name)
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.info),
                            contentDescription = "Center Icon",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp).padding(start = 16.dp)
                        .clickable {
                            coroutineScope.launch { pagerState.animateScrollToPage(1) }
                        },
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.interval),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                    Text(
                        text = convertSecondsToStringResource(lengthOfInterval),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp).padding(start = 16.dp)
                        .clickable {
                            coroutineScope.launch { pagerState.animateScrollToPage(2) }
                        },
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.contraction),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                    Text(
                        text = convertSecondsToStringResource(lengthOfContraction),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = {
                            viewModel.setLengthOfInterval(300)
                            viewModel.setLengthOfContraction(60)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.reset_to_defaults),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f)) // Push the Box to the bottom

                // Image Box aligned at the bottom center
                Box(
                    modifier = Modifier.fillMaxSize()
                    //    .align(Alignment.BottomCenter)
                ) {
                    // Image centered in the Box
                    Image(
                        painter = painterResource(id = R.drawable.bibinomockup),
                        contentDescription = "Bibino",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 16.dp, top = 32.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.created_by_bibino_baby_monitor),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }


            //button
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {

                // Button aligned at the bottom and overlapping the image
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = paddingValues.calculateBottomPadding())
                        .height(72.dp)
                        .width(328.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bm_banner),
                        contentDescription = null,
                        modifier = Modifier.matchParentSize()
                    )
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(Constants.APP_PLAY_STORE_URL_BIBINO_BABY_APP)
                            }
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent, // Set the container color to transparent
                        ),
                        shape = RoundedCornerShape(100),
                        modifier = Modifier.fillMaxSize()
                    ) {
                    }
                }
            }


        }
    }
}


@Preview
@Composable
fun SettingScreenPreview() {
    SettingsScreen(
        navController = NavController(LocalContext.current),
        viewModel = hiltViewModel<SettingsScreenViewModel>(),
        lengthOfInterval = 300,
        lengthOfContraction = 30
    )
}





