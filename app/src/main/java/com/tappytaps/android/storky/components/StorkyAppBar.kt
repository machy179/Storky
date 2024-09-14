@file:OptIn(ExperimentalMaterial3Api::class)

package com.tappytaps.android.storky.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.ui.res.stringResource
import com.tappytaps.android.storky.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorkyAppBar(
    titleNameOfScreen: String = "",
    backgroundColor: Color,
    deleteIconVisible: Boolean = true,
    onDelete: (() -> Unit)? = null,
    closeIconVisible: Boolean = false,
    backArrowIconVisible: Boolean = false,
    onClose: (() -> Unit)? = null,
    nextIconVisible: Boolean = false,
    onNext: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    useMediumTopAppBar: Boolean = true // Parametr pro volbu typu AppBar
) {
    // Společná část pro titulek
    val titleContent: @Composable () -> Unit = {
        Text(
            text = titleNameOfScreen,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    // Společná část pro navigační ikony
    val navigationIconContent: @Composable () -> Unit = {
        if (closeIconVisible) {
            IconButton(onClick = { onClose?.invoke() }) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Close Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        } else if (backArrowIconVisible) {
            IconButton(onClick = { onClose?.invoke() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Arrow Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    // Společná část pro akce
    val actionsContent: @Composable RowScope.() -> Unit = {
        // Delete icon
        if (deleteIconVisible) {
            IconButton(onClick = { onDelete?.invoke() }) {
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "Delete Icon",
                    tint = if (backgroundColor == MaterialTheme.colorScheme.primary)
                        MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Next icon
        if (nextIconVisible) {
            TextButton(onClick = { onNext?.invoke() }) {
                Text(
                    text = stringResource(id = R.string.skip_button),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    // Zobrazení TopAppBar nebo MediumTopAppBar na základě parametru
    if (useMediumTopAppBar) {
        MediumTopAppBar(
            title = titleContent,
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = backgroundColor,
                scrolledContainerColor = backgroundColor
            ),
            navigationIcon = navigationIconContent,
            actions = actionsContent, // RowScope je vyžadováno zde
            modifier = Modifier.fillMaxWidth(),
            scrollBehavior = scrollBehavior
        )
    } else {
        TopAppBar(
            title = titleContent,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundColor,
                scrolledContainerColor = backgroundColor
            ),
            navigationIcon = navigationIconContent,
            actions = actionsContent, // RowScope je vyžadováno zde
            modifier = Modifier.fillMaxWidth(),
            scrollBehavior = scrollBehavior
        )
    }
}




