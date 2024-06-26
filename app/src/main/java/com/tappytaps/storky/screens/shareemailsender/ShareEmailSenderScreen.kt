package com.tappytaps.storky.screens.shareemailsender

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tappytaps.storky.R
import com.tappytaps.storky.components.StorkyAppBar
import com.tappytaps.storky.navigation.StorkyScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareEmailSenderScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = androidx.compose.ui.Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StorkyAppBar(
                titleNameOfScreen = stringResource(R.string.share),
                backgroundColor = MaterialTheme.colorScheme.background,
                closeIconVisible = true,
                deleteIconVisible = false,
                onClose = {
                    navController.navigate(StorkyScreens.HomeScreen.name)
                }
            )
        },
    ) { paddingValues ->
        openEmailer(paddingValues = paddingValues)
    }
}

@Composable
fun openEmailer(paddingValues: PaddingValues) {

    // in the below line, we are
    // creating variables for URL
    val senderEmail = remember {
        mutableStateOf(TextFieldValue())
    }
    val emailSubject = remember {
        mutableStateOf(TextFieldValue())
    }
    val emailBody = remember {
        mutableStateOf(TextFieldValue())
    }

    // on below line we are creating
    // a variable for a context
    val ctx = LocalContext.current

    // on below line we are creating a column
    Column(
        // on below line we are specifying modifier
        // and setting max height and max width
        // for our column
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth()
            // on below line we are
            // adding padding for our column
            .padding(paddingValues),
        // on below line we are specifying horizontal
        // and vertical alignment for our column
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // on the below line, we are
        // creating a text field.
        TextField(
            // on below line we are specifying
            // value for our  text field.
            value = senderEmail.value,

            // on below line we are adding on value
            // change for text field.
            onValueChange = { senderEmail.value = it },

            // on below line we are adding place holder as text
            placeholder = { Text(text = "Enter sender email address") },

            // on below line we are adding modifier to it
            // and adding padding to it and filling max width
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

            // on below line we are adding text style
            // specifying color and font size to it.
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            // on below line we are
            // adding single line to it.
            singleLine = true,
        )
        // on below line adding a spacer.
        Spacer(modifier = Modifier.height(10.dp))

        // on the below line, we are creating a text field.
        TextField(
            // on below line we are specifying
            // value for our  text field.
            value = emailSubject.value,

            // on below line we are adding on value change
            // for text field.
            onValueChange = { emailSubject.value = it },

            // on below line we are adding place holder as text
            placeholder = { Text(text = "Enter email subject") },

            // on below line we are adding modifier to it
            // and adding padding to it and filling max width
            modifier = Modifier.fillMaxWidth(),

            // on below line we are adding text style
            // specifying color and font size to it.
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            // on below line we are
            // adding single line to it.
            singleLine = true,
        )

        // on below line adding a spacer.
        Spacer(modifier = Modifier.height(10.dp))

        // on the below line, we are creating a text field.
        TextField(
            // on below line we are specifying
            // value for our  text field.
            value = emailBody.value,

            // on below line we are adding on value
            // change for text field.
            onValueChange = { emailBody.value = it },

            // on below line we are adding place holder as text
            placeholder = { Text(text = "<html>\n" +
                    "<head></head>\n" +
                    "<body>\n" +
                    "    <p style=\"color: red;\">Tento text je červený a <strong>tučný</strong>.</p>\n" +
                    "</body>\n" +
                    "</html>") },

            // on below line we are adding modifier to it
            // and adding padding to it and filling max width
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

            // on below line we are adding text style
            // specifying color and font size to it.
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            // on below line we are
            // adding single line to it.
            singleLine = true,
        )

        // on below line adding a spacer.
        Spacer(modifier = Modifier.height(20.dp))

        // on below line adding a
        // button to send an email
        Button(onClick = {

            // on below line we are creating
            // an intent to send an email
            val i = Intent(Intent.ACTION_SEND)
            val htmlContent = "<html><body><p style=\"color: red;\">Tento text je <strong>červený a tučný</strong>.</p></body></html>"
            val htmlSubject = "Storky indicator contractions"
            val htmlEmailReceiver = "machy79@seznam.cz"

            // on below line we are passing email address,
            // email subject and email body
            val emailAddress = arrayOf(senderEmail.value.text)
            i.putExtra(Intent.EXTRA_EMAIL,emailAddress)
            i.putExtra(Intent.EXTRA_SUBJECT,htmlSubject)
            i.putExtra(Intent.EXTRA_TEXT,htmlContent)

            // on below line we are
            // setting type of intent
            i.setType("message/rfc822")

            // on the below line we are starting our activity to open email application.
            ctx.startActivity(Intent.createChooser(i,"Choose an Email client : "))

        }) {
            // on the below line creating a text for our button.
            Text(
                // on below line adding a text ,
                // padding, color and font size.
                text = "Send Email",
                modifier = Modifier.padding(10.dp),
                color = Color.White,
                fontSize = 15.sp
            )
        }
    }
}