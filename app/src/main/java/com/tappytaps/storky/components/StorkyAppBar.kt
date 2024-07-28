@file:OptIn(ExperimentalMaterial3Api::class)

package com.tappytaps.storky.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.tappytaps.storky.R
import androidx.compose.material3.*
import androidx.compose.ui.res.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorkyAppBar(
    //AppBar for whole app, instead for Main screen - here is used MainScreenAppBar
    titleNameOfScreen: String = "",
    backgroundColor: Color,
    deleteIconVisible: Boolean = true,
    onDelete: (() -> Unit)? = null,
    closeIconVisible: Boolean = false,
    onClose: (() -> Unit)? = null,
    nextIconVisible: Boolean = false,
    onNext: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {

    MediumTopAppBar(
        title = {
            Text(
                text = titleNameOfScreen,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            scrolledContainerColor = backgroundColor,

            ),
        navigationIcon = {
            if (closeIconVisible) {
                IconButton(
                    onClick = { onClose?.invoke() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "Delete Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

        },
        actions = {


            // Delete icon
            if (deleteIconVisible) {
                IconButton(
                    onClick = { onDelete?.invoke() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "Delete Icon",
                        tint = if (backgroundColor == MaterialTheme.colorScheme.primary) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Next icon
            if (nextIconVisible) {
                TextButton(
                    onClick = {
                        if (onNext != null) {
                            onNext.invoke()
                        }
                    },
                    modifier = Modifier
                ) {
                    Text(
                        text = stringResource(id = R.string.skip_button),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }


        },
        modifier = Modifier
            .fillMaxWidth(),
        scrollBehavior = scrollBehavior
    )


}


