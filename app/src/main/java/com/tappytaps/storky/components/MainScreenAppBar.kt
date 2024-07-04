package com.tappytaps.storky.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tappytaps.storky.R
import com.tappytaps.storky.navigation.StorkyScreens
import com.tappytaps.storky.utils.Constants.SHARE_APP_TEXT


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenAppBar(
    //AppBar just for Main screen, other screens in the app is used StorkyAppBar
    navController: NavController,
    backgroundColor: Color,
    onPause: (() -> Unit)? = null,
    pauseIconVisible: Boolean = true,
    onHistory: (() -> Unit)? = null,
    onNewMonitoring: (() -> Unit)? = null,
    onIndicatorActive: (() -> Unit)? = null,
    intervalsOk: Boolean = false,
    contractionsOk: Boolean = false,
    intervalBetweenTextSetting: String = "5 min",
    intervalBetweenTextCurrent: String = "12 min 8 sec",
    intervalContractionTextSetting: String = "1 min",
    intervalContractionTextCurrent: String = "36 sec",
    showDialogAutomatically: Boolean,
    onDismisStorkyPopUpDialog: (() -> Unit)? = null,

    ) {
    var menuExpanded by remember { mutableStateOf(false) }
    var popUpDialogExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current



    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                // This Box is for centering the heart icon
                // Stork icon
                IconButton(
                    onClick = {
                        popUpDialogExpanded = !popUpDialogExpanded
                        onIndicatorActive?.invoke()
                    },
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Icon(
                        painter = if (intervalsOk && contractionsOk) painterResource(id = R.drawable.indicator_active) else painterResource(
                            id = R.drawable.indicator_inactive
                        ),
                        contentDescription = "Center Icon",
                        tint = if (intervalsOk && contractionsOk) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }

                if (popUpDialogExpanded || showDialogAutomatically) {
                    StorkyPopUpDialog(
                        intervalBetweenTextSetting = intervalBetweenTextSetting,
                        intervalBetweenTextCurrent = intervalBetweenTextCurrent,
                        intervalContractionTextSetting = intervalContractionTextSetting,
                        intervalContractionTextCurrent = intervalContractionTextCurrent,
                        intervalsOk = intervalsOk,
                        contractionsOk = contractionsOk,
                        onLearnMore = {
                            navController.navigate(StorkyScreens.IndicatorHelpScreen.name)
                        },
                        onDismiss = {
                            popUpDialogExpanded = false
                            onDismisStorkyPopUpDialog?.invoke()
                        }
                    )


                }


            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,

            ),
        navigationIcon = {
            // Menu icon
            IconButton(
                onClick = { menuExpanded = true }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Box(
                modifier = Modifier.padding(start = 16.dp, top = 50.dp),
                contentAlignment = Alignment.Center
            ) {
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerLow
                        )
                        .padding(bottom = 16.dp)
                        .width(230.dp) //pořešit šířku, nevím


                ) {
                    if (pauseIconVisible) {
                        StorkyDropMenuItem(
                            icon = R.drawable.pause,
                            text = R.string.pause,
                            onClick = {
                                onPause?.invoke()
                                menuExpanded = false
                            })
                    }


                    StorkyDropMenuItem(
                        icon = R.drawable.restart,
                        text = R.string.restart,
                        onClick = {
                            onNewMonitoring?.invoke()
                            menuExpanded = false
                        })

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                    )

                    StorkyDropMenuItem(
                        icon = R.drawable.help,
                        text = R.string.how_to,
                        onClick = {
                            menuExpanded = false
                            navController.navigate(StorkyScreens.HowToScreen.name)
                        })

                    StorkyDropMenuItem(
                        icon = R.drawable.settings,
                        text = R.string.settings,
                        onClick = {
                            menuExpanded = false
                            navController.navigate(StorkyScreens.SettingsScreen.name)
                        })
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                    )

                    StorkyDropMenuItem(
                        icon = R.drawable.noads,
                        text = R.string.remove_ads,
                        primaryColorIcon = true,
                        onClick = {
                            menuExpanded = false
                            navController.navigate(StorkyScreens.RemoveAdsScreen.name)
                        })

                    StorkyDropMenuItem(
                        icon = R.drawable.hearh,
                        text = R.string.after_birth,
                        primaryColorIcon = true,
                        onClick = {
                            menuExpanded = false
                            navController.navigate(StorkyScreens.BibinoAppScreen.name)
                        })

                    StorkyDropMenuItem(
                        icon = R.drawable.share,
                        text = R.string.share_app,
                        primaryColorIcon = true,
                        onClick = {
                            menuExpanded = false
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, SHARE_APP_TEXT)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share via"))
                        })

                }
            }


        },
        actions = {
            // History icon
            IconButton(
                onClick = { onHistory?.invoke() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.history),
                    contentDescription = "History Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }


        }
    )


}

@Composable
fun StorkyDropMenuItem(
    icon: Int,
    text: Int,
    primaryColorIcon: Boolean = false,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                //       modifier = Modifier.padding(start = 8.dp)
                modifier = Modifier.padding(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = icon), // Replace with your desired icon
                    contentDescription = stringResource(id = text),
                    //       tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(top = 0.dp),
                    tint = if (primaryColorIcon) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                //             Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = text),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp, top = 0.dp)
                )
            }
        },
        onClick = {
            // Handle option 1 click
            onClick()
        }
    )
}