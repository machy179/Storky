package com.tappytaps.android.storky.screens.removeads

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tappytaps.android.storky.R
import com.tappytaps.android.storky.components.StorkyAppBar
import com.tappytaps.android.storky.components.UniversalButton
import com.tappytaps.android.storky.components.ImageTitleContentText
import com.tappytaps.android.storky.navigation.StorkyScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun RemoveAdsScreen(
    navController: NavController,
    viewModel: RemoveAdsScreenViewModel,
    adsDisabled: Boolean,
) {

    val context = LocalContext.current
    val activity = context as? Activity

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var purchaseInProgress = viewModel.purchaseInProgress.value

    val snackbarHostState = remember { SnackbarHostState() }
    val purchaseMessage = stringResource(id = R.string.toast_purchase_made)
    //check if user bought "remove-ads"
    LaunchedEffect(adsDisabled) {
        if (adsDisabled) {
            snackbarHostState.showSnackbar(
                message = purchaseMessage,
                duration = SnackbarDuration.Short
            )

      //      Toast.makeText(context,  purchaseMessage, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState,
            snackbar = { snackbarData -> CustomSnackbar(text = purchaseMessage) }) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            StorkyAppBar(
                backgroundColor = MaterialTheme.colorScheme.background,
                closeIconVisible = true,
                deleteIconVisible = false,
                onClose = {
                    navController.navigate(StorkyScreens.HomeScreen.name)
                },

                scrollBehavior = scrollBehavior
            )

        },
    ) {

        Box(
            modifier = Modifier.fillMaxSize().padding(start = 24.dp, end = 24.dp)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(), // Fill the entire screen
                verticalArrangement = Arrangement.SpaceBetween // Space between top and bottom
            ) {

            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally // Center images horizontally
            ) {

                val imageResId: Int = R.drawable.ads
                val titleResId: Int = R.string.remove_ads_screen_title
                val textResId: Int = R.string.remove_ads_screen_text


                ImageTitleContentText(
                    imageResId = imageResId,
                    titleResId = titleResId,
                    textResId = textResId,
                    modifier = Modifier.fillMaxWidth() // Ensure it fills the width
                )

            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                if (!purchaseInProgress) {

                    if (!adsDisabled) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {

                            UniversalButton(
                                text = stringResource(id = R.string.remove_ads_screen_button_text),
                                subText = stringResource(id = R.string.remove_ads_screen_button_subtext),
                                onClick = {
                                    activity?.let {
                                        Log.d("remove ads Storky", "1")
                                        viewModel.startPurchase(it)
                                    }
                                },
                                disableInsetNavigationBarPadding = true,
                                bottomSpacer = false,
                                sendButton = true
                            )
                        }
                    }


                    Spacer(
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            //TODO
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.restore_purchase),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Spacer(
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally // Ensures the text is centered
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append(stringResource(id = R.string.you_agree_to_our) + " ")
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                    append(stringResource(id = R.string.terms_of_service) + " ")
                                }
                                append(stringResource(id = R.string.and) + " ")
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                    append(stringResource(id = R.string.privacy_policy))
                                }
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center, // Ensures the text is centered
                            modifier = Modifier.fillMaxWidth().clickable {
                                //TODO
                            }
                        )
                    }



                } else {


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 100.dp), // Adjust padding to match the previous layout
                        contentAlignment = Alignment.Center // Centers content both horizontally and vertically
                    ) {
                        CircularProgressIndicator()
                    }


                }


            }


        }
    }
}

@Composable
fun CustomSnackbar(text: String) {
    Snackbar(
        modifier = Modifier.padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(24.dp)
    ),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurface,

    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp),
            textAlign = TextAlign.Center // Center the text horizontally
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RemoveAdsPreview() {
    RemoveAdsScreen(
        NavController(LocalContext.current),
        viewModel = hiltViewModel<RemoveAdsScreenViewModel>(),
        adsDisabled = false
    )
}