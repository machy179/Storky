package com.tappytaps.android.storky.screens.bibinoapp

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tappytaps.android.storky.R
import com.tappytaps.android.storky.components.StorkyAppBar
import com.tappytaps.android.storky.navigation.StorkyScreens
import com.tappytaps.android.storky.utils.Constants.APP_PLAY_STORE_URL_BIBINO_BABY_APP

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibinoAppScreen(
    navController: NavController,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StorkyAppBar(
                titleNameOfScreen = stringResource(R.string.bibino_app),
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

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues)
                    .padding(start = 16.dp, end = 16.dp)
            ) {


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.bibino_app_text),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // Push the Box to the bottom
            }

            // Image Box aligned at the bottom center
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)

            ) {
                // Image centered in the Box
                Image(
                    painter = painterResource(id = R.drawable.bibinomockup),
                    contentDescription = "Bibino",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )

                // Button aligned at the bottom and overlapping the image
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 60.dp) // Adjust padding as needed
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
                                data = Uri.parse(APP_PLAY_STORE_URL_BIBINO_BABY_APP)
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
fun BibinoAppScreenPreview() {
    BibinoAppScreen(
        navController = NavController(LocalContext.current)
    )
}




