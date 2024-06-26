package com.tappytaps.storky.screens.indicatorhelp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tappytaps.storky.R
import com.tappytaps.storky.components.StorkyAppBar
import com.tappytaps.storky.navigation.StorkyScreens
import com.tappytaps.storky.ui.theme.WhiteColor
import com.tappytaps.storky.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndicatorHelpScreen(
    navController: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StorkyAppBar(
                titleNameOfScreen = stringResource(R.string.storky_indicator),
                backgroundColor = MaterialTheme.colorScheme.background,
                closeIconVisible = true,
                deleteIconVisible = false,
                onClose = {
                    navController.navigate(StorkyScreens.HomeScreen.name)
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
        ) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.how_it_works_title),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.how_it_works_text),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier,
                            //       horizontalArrangement = Arrangement.spacedBy(8.dp),
                            //       horizontalArrangement = Arrangement.End
                        ) {
                            Box(
                                modifier = Modifier.width(80.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally // Centers content horizontally
                                ) {
                                    WhiteStorkBullet(conditionsMet = false)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = stringResource(R.string.conditions_not_met_title),
                                        style = MaterialTheme.typography.labelMedium,
                                        textAlign = TextAlign.Center
                                    )
                                }
                        }
                            Spacer(modifier = Modifier.height(24.dp))
                            Box(
                                modifier = Modifier.width(80.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally // Centers content horizontally
                                ) {
                                    WhiteStorkBullet(conditionsMet = true)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = stringResource(R.string.conditions_met_title),
                                        style = MaterialTheme.typography.labelMedium,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                    }

                }
                Spacer(modifier = Modifier.height(31.dp))
                Column {
                    Text(
                        text = stringResource(R.string.conditions_title),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.conditions_text),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextButton(
                        onClick = {
                            navController.navigate(StorkyScreens.SettingsScreen.name)
                        },
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.change_conditions),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
                Spacer(modifier = Modifier.height(31.dp))
                Column {
                    Text(
                        text = stringResource(R.string.when_to_go_title),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.when_to_go_text),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextWithBullet(stringResource(R.string.when_to_go_row_1))
                    Spacer(modifier = Modifier.height(8.dp))
                    TextWithBullet(stringResource(R.string.when_to_go_row_2))
                    Spacer(modifier = Modifier.height(8.dp))
                    TextWithBullet(stringResource(R.string.when_to_go_row_3))
                    Spacer(modifier = Modifier.height(8.dp))
                    TextWithBullet(stringResource(R.string.when_to_go_row_4))
                    Spacer(modifier = Modifier.height(8.dp))
                    TextWithBullet(stringResource(R.string.when_to_go_row_5))
                    Spacer(modifier = Modifier.height(8.dp))
                    TextWithBullet(stringResource(R.string.when_to_go_row_6))
                    Spacer(modifier = Modifier.height(8.dp))
                    TextWithBullet(stringResource(R.string.when_to_go_row_7))
                }
            }
        }
    }
}
}

@Composable
fun WhiteStorkBullet(conditionsMet: Boolean) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .shadow(8.dp, CircleShape) // Apply shadow with a specified elevation and shape
            .clip(CircleShape) // Clip the content to the CircleShape to match the background shape
            .background(
                color = WhiteColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center // Centers the icon within the Box
    ) {
        Icon(
            painter = if (conditionsMet) painterResource(id = R.drawable.indicator_active) else painterResource(id = R.drawable.indicator_inactive),
            contentDescription = "Center Icon",
            tint = if (conditionsMet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PinkBullet() {
    Box(
        modifier = Modifier
            .size(9.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
    )
}


@Composable
fun TextWithBullet(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        PinkBullet()
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}


@Preview
@Composable
fun IndicatorHelpScreenPreview() {
    IndicatorHelpScreen(navController = NavController(LocalContext.current))
}

@Preview
@Composable
fun PinkBulletPreview() {
    PinkBullet()
}


