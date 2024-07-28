@file:OptIn(ExperimentalMaterial3Api::class)

package com.tappytaps.storky.screens.email

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.tappytaps.storky.components.CustomDialog
import com.tappytaps.storky.components.EmailInput
import com.tappytaps.storky.components.UniversalButton
import com.tappytaps.storky.components.ImageTitleContentText
import com.tappytaps.storky.components.StorkyAppBar
import com.tappytaps.storky.navigation.StorkyScreens
import com.tappytaps.storky.utils.isValidEmail

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
fun EmailScreen(
    navController: NavController,
    viewModel: EmailScreenViewModel = hiltViewModel(),
) {
    val showDoneEmailScreen = remember { mutableStateOf(false) }
    if (!showDoneEmailScreen.value) {
        AskEmailScreen(navController, showDoneEmailScreen, viewModel)
    } else {
        DoneEmailScreen(navController)
    }


}


@Composable
fun AskEmailScreen(
    navController: NavController,
    showDoneEmailScreen: MutableState<Boolean>,
    viewModel: EmailScreenViewModel,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val showDialogInvalidEmail = remember { mutableStateOf(false) }
    val showDialogSkipStep = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            StorkyAppBar(
                backgroundColor = MaterialTheme.colorScheme.background,
                deleteIconVisible = false,
                nextIconVisible = true,
                onNext = {
                    showDialogSkipStep.value = true
                },

                scrollBehavior = scrollBehavior
            )

        },
    ) {paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .imePadding()
        ) {
            val imageResId: Int = R.drawable.guide
            val titleResId: Int = R.string.email_title
            val textResId: Int = R.string.email_text

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ImageTitleContentText(
                    imageResId = imageResId,
                    titleResId = titleResId,
                    textResId = textResId
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                       // .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val email = rememberSaveable { mutableStateOf("") }

                    EmailInput(
                        emailState = email,
                        onAction = KeyboardActions {
                            if (isValidEmail(email.value)) {
                                showDoneEmailScreen.value = true
                                viewModel.sendEmail(email.value).run {
                                    Log.d("try to send e-mail: ", "email ${email.value} was sent")
                                }
                            } else {
                                showDialogInvalidEmail.value = true
                            }
                        }
                    )
                }
            }
        }
        if (showDialogInvalidEmail.value) {
            CustomDialog(title = stringResource(R.string.invalid_email_address),
                text = stringResource(R.string.enter_correct_email_address),
                secondTextButton = stringResource(R.string.ok),
                firstRequest = { },
                onDismissRequest = { showDialogInvalidEmail.value = false })
        }

        if (showDialogSkipStep.value) {
            CustomDialog(title = stringResource(R.string.skip_step_title),
                text = stringResource(R.string.skip_step_text),
                firstTextButton = stringResource(R.string.skip),
                secondTextButton = stringResource(R.string.dont_skip),
                enableFirstRequest = true,
                firstRequest = {
                    //TODO
                    navController.navigate(StorkyScreens.HomeScreen.name)
                },
                onDismissRequest = { showDialogSkipStep.value = false })
        }

}
}

@Composable
fun DoneEmailScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {


        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally // Center images horizontally
        ) {

            val imageResId: Int = R.drawable.inbox
            val titleResId: Int = R.string.check_your_inbox_title
            val textResId: Int = R.string.check_your_inbox_text


            ImageTitleContentText(
                imageResId = imageResId,
                titleResId = titleResId,
                textResId = textResId,
                widthOfColumn = 302.dp
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            UniversalButton(
                text = stringResource(id = R.string.got_it),
                onClick = {
                    navController.navigate(StorkyScreens.HomeScreen.name)
                }
            )
        }


    }
}


@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Preview
@Composable
fun EmailScreenPreview() {
    EmailScreen(NavController(LocalContext.current))
}