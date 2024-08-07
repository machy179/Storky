package com.tappytaps.android.storky.screens.trybibino

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.tappytaps.android.storky.R
import com.tappytaps.android.storky.components.UniversalButton
import com.tappytaps.android.storky.components.ImageTitleContentText
import com.tappytaps.android.storky.components.StorkyAppBar
import com.tappytaps.android.storky.navigation.StorkyScreens
import com.tappytaps.android.storky.utils.Constants


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@Composable
fun TryBibinoScreen(navController: NavController) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            StorkyAppBar(
                backgroundColor = MaterialTheme.colorScheme.background,
                deleteIconVisible = false,
                nextIconVisible = true,
                onNext = {
                    navController.navigate(StorkyScreens.EmailScreen.name)
                },

                scrollBehavior = scrollBehavior
            )

        },
    ) {

    ContentPageTryBibiono()
    }
}

@Composable
fun ContentPageTryBibiono() {

    Box(modifier = Modifier.fillMaxSize()) {



        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally // Center images horizontally
        ) {

            val imageResId: Int = R.drawable.bibino
            val titleResId: Int = R.string.try_bibino_title
            val textResId: Int = R.string.try_bibino_text


            ImageTitleContentText(
                imageResId = imageResId,
                titleResId = titleResId,
                textResId = textResId,
                modifier = Modifier.fillMaxWidth() // Ensure it fills the width
            )

        }
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            UniversalButton(
                text = stringResource(id = R.string.try_bibino_for_free_button_text),
                onClick = {

                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(Constants.APP_PLAY_STORE_URL_BIBINO_BABY_APP)
                    }
                    context.startActivity(intent)
                }
            )
        }


    }

}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun TryBibinoScreenPreview() {
    TryBibinoScreen(NavController(LocalContext.current))
}
