package com.tappytaps.storky.screens.howto

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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HowToScreen(
    navController: NavController,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StorkyAppBar(
                titleNameOfScreen = stringResource(R.string.how_to),
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
                            text = stringResource(R.string.basic_terms),
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextWithBullet(text = stringResource(R.string.contraction))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.contraction_description),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        TextWithBullet(
                            text = stringResource(R.string.interval),
                            primaryColor = false
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.interval_description),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start
                        )


                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.true_labour),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.true_labour_description),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start
                        )

                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = stringResource(R.string.labour_phases),
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.early_labour_phase),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.early_labour_phase_description),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = stringResource(R.string.active_labour_phase),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.active_labour_phase_description),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = stringResource(R.string.transition_phase),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.transition_phase_description),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = stringResource(R.string.pushing_and_delivery),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.pushing_and_delivery_description),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start
                        )


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
            painter = if (conditionsMet) painterResource(id = R.drawable.indicator_active) else painterResource(
                id = R.drawable.indicator_inactive
            ),
            contentDescription = "Center Icon",
            tint = if (conditionsMet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun PinkBullet(primaryColor: Boolean) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .background(
                color = if (primaryColor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
    )
}


@Composable
private fun TextWithBullet(text: String, primaryColor: Boolean = true) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        PinkBullet(primaryColor = primaryColor)
        Spacer(modifier = Modifier.width(9.dp))
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}


@Preview
@Composable
fun HowToScreenPreview() {
    HowToScreen(navController = NavController(LocalContext.current))
}



