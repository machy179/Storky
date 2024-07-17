package com.tappytaps.storky.screens.removeads

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tappytaps.storky.R
import com.tappytaps.storky.components.StorkyAppBar
import com.tappytaps.storky.components.UniversalButton
import com.tappytaps.storky.components.ImageTitleContentText
import com.tappytaps.storky.navigation.StorkyScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun RemoveAdsScreen(
    navController: NavController,
    viewModel: RemoveAdsScreenViewModel,
) {

    val context = LocalContext.current
    val activity = context as? Activity

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
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
            modifier = Modifier.fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {


            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally // Center images horizontally
            ) {

                var imageResId: Int = R.drawable.ads
                var titleResId: Int = R.string.remove_ads_screen_title
                var textResId: Int = R.string.remove_ads_screen_text


                ImageTitleContentText(
                    imageResId = imageResId,
                    titleResId = titleResId,
                    textResId = textResId,
                    widthOfColumn = 302.dp
                )

            }
            val context = LocalContext.current
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
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
                                Log.d("remove ads Storky","1")
                                viewModel.launchPurchaseFlow(it)
                            }
                            navController.navigate(StorkyScreens.HomeScreen.name)
                        },
                        disableInsetNavigationBarPadding = true
                    )
                }

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
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Row(
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.you_agree_to_our) + " ",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = stringResource(id = R.string.terms_of_service) + " ",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            //TODO
                        }
                    )
                    Text(
                        text = stringResource(id = R.string.and) + " ",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = stringResource(id = R.string.privacy_policy),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            //TODO
                        }
                    )
                }


            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RemoveAdsPreview() {
    RemoveAdsScreen(NavController(LocalContext.current),
        viewModel = hiltViewModel<RemoveAdsScreenViewModel>()
    )
}