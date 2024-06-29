package com.tappytaps.storky.screens.trybibino

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tappytaps.storky.R
import com.tappytaps.storky.components.UniversalButton
import com.tappytaps.storky.components.ImageTitleContentText
import com.tappytaps.storky.navigation.StorkyScreens
import com.tappytaps.storky.utils.Constants
import com.tappytaps.storky.utils.Constants.END_PADDING_NEXT
import com.tappytaps.storky.utils.Constants.TOP_PADDING_NEXT


@ExperimentalFoundationApi
@Composable
fun TryBibinoScreen(navController: NavController) {
    ContentPageTryBibiono(navController)
}

@Composable
fun ContentPageTryBibiono(navController: NavController) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {

        TextButton(
            onClick = {
                navController.navigate(StorkyScreens.EmailScreen.name)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(start = 10.dp, top = TOP_PADDING_NEXT, end = END_PADDING_NEXT, bottom = 12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.next_button),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
        }


        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally // Center images horizontally
        ) {

            var imageResId: Int = R.drawable.bibino
            var titleResId: Int = R.string.try_bibino_title
            var textResId: Int = R.string.try_bibino_text


            ImageTitleContentText(
                imageResId = imageResId,
                titleResId = titleResId,
                textResId = textResId
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
@Preview
@Composable
fun ContentPageTryBibionoPreview() {
    ContentPageTryBibiono(NavController(LocalContext.current))
}
