package com.tappytaps.storky.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.tappytaps.storky.R
import com.tappytaps.storky.model.Contraction
import com.tappytaps.storky.navigation.StorkyScreens
import com.tappytaps.storky.ui.theme.ProgressIndicatorBackgroundColor
import com.tappytaps.storky.ui.theme.WhiteColor
import com.tappytaps.storky.utils.convertCalendarToText
import com.tappytaps.storky.utils.convertSecondsToTimeString
import com.tappytaps.storky.utils.getFormattedDate
import java.util.Calendar


@Composable
fun ImageTitleContentText(
    modifier: Modifier = Modifier,
    imageResId: Int,
    titleResId: Int,
    textResId: Int,
    learnMore: Boolean = false,
    navController: NavController = NavController(LocalContext.current),
    widthOfColumn: Dp = 280.dp,

    bottomSpace: Boolean = false,
) {
    Column(
        modifier = modifier
            .width(widthOfColumn)
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Presentation images",
            modifier = Modifier

                .fillMaxWidth()
            //           .padding(9.5.dp) // Padding around the image
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(titleResId),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center // Center the text horizontally
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(textResId),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center // Center the text horizontally

        )

        if (learnMore) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = {
                        navController.navigate(StorkyScreens.HowToScreen.name)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.learn_more),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center // Center the text horizontally
                    )
                }
            }
        }
        if (bottomSpace) {
            Spacer(modifier = Modifier.height(72.dp))  //it is for History screen
        }
    }
}


@Composable
fun UniversalButton(
    text: String,
    subText: String = "",
    onClick: () -> Unit,
    inverseColor: Boolean = false,
    disableInsetNavigationBarPadding: Boolean = false, //if it is used in HomeScreen on Scaffold, it is necessary to disable bottom padding of Inset NavigationBar
) {

    val modifier = if (disableInsetNavigationBarPadding) {
        Modifier.fillMaxWidth()
    } else {
        Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxWidth()
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
    )
    {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (inverseColor) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(100), // Maximum rounded corners
            modifier = Modifier
                .height(56.dp)
                .width(264.dp)
        ) {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
            )
            {
                Text(
                    text = text,
                    color = if (inverseColor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge
                )
                if (subText != "") {
                    Text(
                        text = subText,
                        color = if (inverseColor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

        }

        Spacer(
            modifier = Modifier.padding(bottom = 12.dp)
        )
    }
}


@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
) {
    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxWidth(), // Ensure Column takes full width
        horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
    )
    {
        InputField(
            modifier = modifier,
            valueState = emailState,
            labelId = labelId,
            keyboardType = KeyboardType.Email,
            imeAction = imeAction,
            onAction = onAction
        )

        /*        Spacer(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                )*/
    }


}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
) {

    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        singleLine = isSingleLine,
        shape = RoundedCornerShape(100.dp), // Rounded corners
        label = { Text(text = stringResource(R.string.enter_email_address)) }, // Label inside the border
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = modifier
            .width(296.dp) // Maximum width
            .height(64.dp), // dle Figma, tam je 56, ale uřezává mi to spodky, pořešit později
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction
    )


}


@Composable
fun CustomDialog(
    title: String = "asdfadsf",
    text: String = "adfad adfadf",
    firstTextButton: String = "aa",
    secondTextButton: String = "bb",
    enableFirstRequest: Boolean = false,
    firstRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    changePositionButtons: Boolean = false, //because in historyscreen, delete dialog there is change position of buttons
) {

    AlertDialog(
        title = {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        text = {
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        dismissButton = {
            if (enableFirstRequest) {
                TextButton(
                    onClick = {
                        if (changePositionButtons) onDismissRequest() else firstRequest()
                    }
                ) {
                    Text(
                        firstTextButton,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (changePositionButtons) firstRequest() else onDismissRequest()

                    onDismissRequest()
                }
            ) {
                Text(
                    secondTextButton,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContractionRowByItems(
    modifier: Modifier = Modifier,
    contraction: Contraction,
    numberOfContraction: Int,
    deleteContraction: () -> Unit,
) {

    var dialogdeleteContractionVisible by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.combinedClickable(
            onClick = { /* No-op or short click action */ },
            onLongClick = { dialogdeleteContractionVisible = true }
        )
    ) {
        ContractionRow(
            lengthOfContraction = contraction.lengthOfContraction,
            contractionTime = contraction.contractionTime,
            timeBetweenContractions = contraction.timeBetweenContractions,
            numberOfContraction = numberOfContraction
        )

        if (dialogdeleteContractionVisible) {
            CustomDialog(
                title = stringResource(R.string.clear_contraction_title),
                text = stringResource(R.string.clear_contraction_text),
                firstTextButton = stringResource(R.string.cancel),
                secondTextButton = stringResource(R.string.clear),
                enableFirstRequest = true,
                firstRequest = {
                    deleteContraction.invoke()
                    dialogdeleteContractionVisible = false
                },
                onDismissRequest = { dialogdeleteContractionVisible = false },
                changePositionButtons = true
            )
        }
    }

}

@Composable
fun ContractionRow(
    modifier: Modifier = Modifier,
    lengthOfContraction: Int,
    contractionTime: Calendar,
    timeBetweenContractions: Int,
    numberOfContraction: Int,
) {
    Surface(
        modifier
            .padding(12.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TableViewCellContraction(
                        modifier = Modifier.weight(1f),
                        numberOfContraction = numberOfContraction,
                        time = lengthOfContraction
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center // Center content vertically
                    ) {
                        if (getFormattedDate(contractionTime) != stringResource(R.string.today)) {
                            Text(
                                text = getFormattedDate(contractionTime),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.align(Alignment.CenterHorizontally) // Center horizontally
                            )
                        }

                        Text(
                            text = convertCalendarToText(contractionTime),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.CenterHorizontally) // Center horizontally
                        )
                    }


                    TableViewCellContraction(
                        modifier = Modifier.weight(1f),
                        time = timeBetweenContractions,
                        visibleTimeBetweenContractions = true,
                        maxLength = 300
                    )

                }
            }
        }
    }
}

@Composable
fun TableViewCellContraction(
    modifier: Modifier,
    numberOfContraction: Int = 0,
    time: Int, //lengthOfContraction or timeBetweenContractions
    visibleTimeBetweenContractions: Boolean = false, //if is used for timeBetweenContractions, is true
    maxLength: Int = 60,
) {
    val maxLength = maxLength

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = if (visibleTimeBetweenContractions) Alignment.End else Alignment.Start
            //    verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (visibleTimeBetweenContractions) stringResource(R.string.interval) else stringResource(
                    R.string.contraction
                ) + " ${numberOfContraction.toString()}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = if (time == -1) "-:--"
                else if (time > 1200) "> 20:00" else convertSecondsToTimeString(time),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            val progress = time / maxLength.toFloat()
            Log.d("Storky progress 1: ", progress.toString())
            CustomProgressBar(
                progress = progress,
                reverseProgress = visibleTimeBetweenContractions
            )
            //LinearProgressIndicator was not used, because is not possible to reverse indicator

        }
    }
}

@Composable
fun StorkyPopUpDialog(
    intervalBetweenTextSetting: String = "5 min",
    intervalBetweenTextCurrent: String = "12 min 8 sec",
    intervalContractionTextSetting: String = "1 min",
    intervalContractionTextCurrent: String = "36 sec",
    intervalsOk: Boolean = false,
    contractionsOk: Boolean = false,
    onLearnMore: () -> Unit,
    onDismiss: () -> Unit,
) {
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true),

        ) {

        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 54.dp)
                .shadow( // Add shadow for elevation
                    elevation = 4.dp, // Adjust elevation as needed
                    shape = RoundedCornerShape(24.dp), // Shape of the shadow
                    clip = false // Whether to clip the shadow to the shape
                )
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                //    verticalArrangement = Arrangement.Top
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = if (intervalsOk && contractionsOk) stringResource(R.string.conditions_met_title) else stringResource(
                            R.string.conditions_not_met_title
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(start = 24.dp, end = 24.dp, top = 24.dp),
                        textAlign = TextAlign.Center // Center the text horizontally
                    )
                    //  Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (intervalsOk && contractionsOk) stringResource(R.string.conditions_met_text) else stringResource(
                            R.string.conditions_not_met_text
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(start = 24.dp, end = 24.dp),
                        textAlign = TextAlign.Center // Center the text horizontally
                    )
                    //   Spacer(modifier = Modifier.height(16.dp))
                }


                Box(
                    modifier = Modifier.align(Alignment.End)
                        .padding(start = 30.dp, top = 30.dp, end = 30.dp, bottom = 0.dp)


                ) {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        //    verticalArrangement = Arrangement.Top
                    ) {

                        Row(
                            modifier = Modifier,
                            //       horizontalArrangement = Arrangement.spacedBy(8.dp),
                            //       horizontalArrangement = Arrangement.End
                        ) {
                            if (intervalsOk) {
                                ConditionsOkIcon(
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .align(Alignment.CenterVertically)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.conditions_no),
                                    contentDescription = "Center Icon",
                                    modifier = Modifier.padding(end = 16.dp)
                                        .align(Alignment.CenterVertically),
                                    //     tint = if (intervalsOk) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    //     tint = if (intervalsOk) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

                                )
                            }


                            Column(
                                modifier = Modifier
                            ) {
                                Text(
                                    text = stringResource(R.string.intervals_should_be_shorter_than) + " " + intervalBetweenTextSetting,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(R.string.now) + " " + intervalBetweenTextCurrent,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }


                        }


                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier,
                            //       horizontalArrangement = Arrangement.spacedBy(8.dp),
                            //       horizontalArrangement = Arrangement.End
                        ) {
                            if (contractionsOk) {
                                ConditionsOkIcon(
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .align(Alignment.CenterVertically)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.conditions_no),
                                    contentDescription = "Center Icon",
                                    modifier = Modifier.padding(end = 16.dp)
                                        .align(Alignment.CenterVertically),
                                    //     tint = if (contractionsOk) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Column(
                                modifier = Modifier
                            ) {
                                Text(
                                    text = stringResource(R.string.contractions_should_be_shorter_than) + " " + intervalContractionTextSetting,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(R.string.now) + " " + intervalContractionTextCurrent,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
                    onClick = {
                        onDismiss()
                        onLearnMore()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.learn_more),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }


            }
        }

    }
}

@Composable
fun ConditionsOkIcon(modifier: Modifier) {
    Box(
        modifier = modifier
            .size(32.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Rounded.Done,
            contentDescription = "Confirm",
            tint = WhiteColor,
            modifier = Modifier
                .align(Alignment.Center)
                .size(18.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConditionsOkIcon() {
    ConditionsOkIcon(modifier = Modifier)
}


@Composable
fun CustomProgressBar(
    progress: Float = 1f,
    reverseProgress: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .height(7.9.dp)
                .background(ProgressIndicatorBackgroundColor)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = if (reverseProgress) Arrangement.End else Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .height(7.9.dp)
                        .background(
                            if (reverseProgress) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                        )
                        .fillMaxWidth(progress)
                )
            }
        }
    }
}


@Preview
@Composable
private fun CustomDialogPreview() {
    CustomDialog(firstRequest = { },
        enableFirstRequest = true,
        onDismissRequest = { }
    )
}

@Preview
@Composable
private fun UniversalButtonPreview() {
    UniversalButton("text", onClick = { })
}

@Preview
@Composable
fun ContractionRowPreview() {
    ContractionRow(
        lengthOfContraction = 16,
        contractionTime = Calendar.getInstance(),
        timeBetweenContractions = 60,
        numberOfContraction = 3
    )
}

@Preview
@Composable
fun StorkyPopUpDialogPreview() {
    Box(
        modifier = Modifier.padding(195.dp)
    ) {
        StorkyPopUpDialog(
            onLearnMore = { },
            onDismiss = { },
            contractionsOk = true,
            intervalsOk = false
        )
    }

}

