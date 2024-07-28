package com.tappytaps.storky.screens.history

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tappytaps.storky.R
import com.tappytaps.storky.components.ContractionRow
import com.tappytaps.storky.components.ContractionRowByItems
import com.tappytaps.storky.components.CustomDialog
import com.tappytaps.storky.components.StorkyAppBar
import com.tappytaps.storky.components.StorkyDropMenuItem
import com.tappytaps.storky.components.ImageTitleContentText
import com.tappytaps.storky.components.StorkyNativeAdView
import com.tappytaps.storky.model.Contraction
import com.tappytaps.storky.navigation.StorkyScreens
import com.tappytaps.storky.screens.home.HomeScreenViewModel
import com.tappytaps.storky.utils.getDateInHistory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    historyViewModel: HistoryScreenViewModel,
    listOfContractionsHistory: List<Contraction>,
    adsDisabled: Boolean,
    listOfActiveContractions: List<Contraction>,
    homeViewModel: HomeScreenViewModel,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var dialogClearAllDataVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val deleteIconVisible =
        remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            StorkyAppBar(
                titleNameOfScreen = stringResource(R.string.history),
                backgroundColor = MaterialTheme.colorScheme.background,
                deleteIconVisible = deleteIconVisible.value,
                onDelete = {
                    dialogClearAllDataVisible = true
                },
                closeIconVisible = true,
                onClose = {
                    navController.navigate(StorkyScreens.HomeScreen.name)
                },

                scrollBehavior = scrollBehavior
            )

        },
    ) { paddingValues ->
        val rowOfActualContraction =
            remember { mutableStateOf(false) } //...if it is possible to show row of actual contraction

        if (homeViewModel.pauseStopWatch.value == true || homeViewModel.isRunning.value == true) {
            rowOfActualContraction.value = true
        } else {
            rowOfActualContraction.value = false
        }


        if (listOfContractionsHistory.isNullOrEmpty()
            && listOfActiveContractions.isNullOrEmpty()
            && rowOfActualContraction.value == false
        ) { //nothing to show, so show "It seems so empty"

            deleteIconVisible.value = false

            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center),//.padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally, // Center images horizontally
                    verticalArrangement = Arrangement.Center
                ) {

                    val imageResId: Int = R.drawable.empty
                    val titleResId: Int = R.string.history_title
                    val textResId: Int = R.string.history_text


                    ImageTitleContentText(
                        imageResId = imageResId,
                        titleResId = titleResId,
                        textResId = textResId,
                        bottomSpace = true
                    )

                }
            }
        } else {

            deleteIconVisible.value = true
            val listState = rememberLazyListState()
            LaunchedEffect(key1 = Unit) {
                listState.animateScrollToItem(index = 0) //it is for roll lazycolumn to top
            }

            val listOfSetsOfContractions: List<Int> = //to create list of all sets
                listOfContractionsHistory
                    .map { it.in_set }
                    .distinct()



            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.Top // Start items from the top
            ) {

                //actual contractions:
                //...if no listOfActiveContractions, just one row of actual contraction
                val sizeListOfActiveContractions = listOfActiveContractions.size
                if (sizeListOfActiveContractions == 0
                    && rowOfActualContraction.value
                ) {
                    item {
                        FirstRowOfSetsOfActiveContractions(
                            listOfActiveContractions,
                            homeViewModel,
                            context
                        )
                    }
                }

                //actual contractions:
                //...in listOfActiveContractions is more than one Contraction
                itemsIndexed(listOfActiveContractions,
                    key = { index, activeContraction -> activeContraction.id }
                ) { index, contraction ->
                    val reversedIndex = sizeListOfActiveContractions - index
                    if (index == 0
                    ) {
                        FirstRowOfSetsOfActiveContractions(
                            listOfActiveContractions,
                            homeViewModel,
                            context
                        )
                    }


                    ContractionRowByItems(
                        contraction = contraction,
                        numberOfContraction = reversedIndex,
                        deleteContraction = { historyViewModel.deleteContraction(contraction) }
                    )

                    if ((index == 1 && !rowOfActualContraction.value)
                        ||
                        (index == 0 && rowOfActualContraction.value)
                    ) {
                        if (!adsDisabled) {
                            StorkyNativeAdView()
                        }


                    }
                }

                //history contractions
                itemsIndexed(listOfSetsOfContractions) { index, inSetOfContractions ->
                    val filteredContractions =
                        listOfContractionsHistory.filter { it.in_set == inSetOfContractions }

                    Spacer(modifier = Modifier.height(24.dp))

                    BarSetsContractions(
                        listOfContractions = filteredContractions,
                        id = inSetOfContractions,
                        deleteSetOfContractions = {
                            historyViewModel.deleteSetOfHistory(inSetOfContractions)
                        },
                        shareSetOfContractions = {
                            historyViewModel.shareSetsOfHistoryContractionsByEmail(
                                context = context,
                                contractionInSet = inSetOfContractions
                            )
                        }
                    )

                    val totalContractions = filteredContractions.size
                    for ((idx, contraction) in filteredContractions.withIndex()) {
                        val descendingIndex = totalContractions - idx
                        ContractionRowByItems(
                            contraction = contraction,
                            numberOfContraction = descendingIndex,
                            deleteContraction = { historyViewModel.deleteContraction(contraction) }
                        )

                    }
                }


            }
            if (dialogClearAllDataVisible) {
                CustomDialog(
                    title = stringResource(R.string.clear_all_data_title),
                    text = stringResource(R.string.clear_all_data_text),
                    firstTextButton = stringResource(R.string.cancel),
                    secondTextButton = stringResource(R.string.clear),
                    enableFirstRequest = true,
                    firstRequest = {
                        historyViewModel.deleteAllHistory()
                        homeViewModel.deleteActualSet(0)
                        dialogClearAllDataVisible = false
                    },
                    onDismissRequest = { dialogClearAllDataVisible = false },
                    changePositionButtons = true
                )
            }
        }


    }
}

@Composable
private fun FirstRowOfSetsOfActiveContractions(
    //first row of each sets - date and submenu
    listOfActiveContractions: List<Contraction>,
    homeViewModel: HomeScreenViewModel,
    context: Context,
) {
    BarSetsContractions(
        listOfContractions = listOfActiveContractions,
        id = 0,
        deleteSetOfContractions = { homeViewModel.deleteActualSet(0) },
        shareSetOfContractions = {
            homeViewModel.shareSetsOfActualContractionsByEmail(
                context = context,
                contractionInSet = 0,
                currentContractionLength = homeViewModel.currentContractionLength.value,
                currentTimeDateContraction = homeViewModel.currentTimeDateContraction.value
            )
        }

    )

    if (homeViewModel.currentContractionLength.value > 0 || homeViewModel.currentLengthBetweenContractions.value > 0) {
        ContractionRow(
            lengthOfContraction = homeViewModel.currentContractionLength.value,
            contractionTime = homeViewModel.currentTimeDateContraction.value,
            timeBetweenContractions = homeViewModel.currentLengthBetweenContractions.value,
            numberOfContraction = listOfActiveContractions.size + 1
        )

    }
}


@Composable
private fun BarSetsContractions(
    listOfContractions: List<Contraction>? = null,
    id: Int,
    deleteSetOfContractions: () -> Unit,
    shareSetOfContractions: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(), // Ensure the Row takes up the full width
        horizontalArrangement = Arrangement.SpaceBetween // Space out items to opposite ends
    ) {
        var menuExpanded by remember { mutableStateOf(false) }
        var dialogClearThisSetVisible by remember { mutableStateOf(false) }
        Text(
            text = if (listOfContractions.isNullOrEmpty()) {
                stringResource(R.string.today)
            } else {

                getDateInHistory(
                    listOfContractions.filter { it.in_set == id }
                        .last().contractionTime,
                    listOfContractions.filter { it.in_set == id }
                        .first().contractionTime
                )
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(
                start = 15.dp,
                top = 12.dp,
                bottom = 12.dp
            )
        )
        IconButton(
            onClick = {
                menuExpanded = true
            },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.dots),
                contentDescription = "Center Icon",
                modifier = Modifier
                // tint = if (indicatorActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerLow
                    )

            ) {
                StorkyDropMenuItem(
                    icon = R.drawable.share,
                    text = R.string.share,
                    onClick = {
                        menuExpanded = false
                        shareSetOfContractions.invoke()
                    })

                StorkyDropMenuItem(
                    icon = R.drawable.delete,
                    text = R.string.delete,
                    onClick = {
                        menuExpanded = false
                        dialogClearThisSetVisible = true
                    })

            }


        }
        if (dialogClearThisSetVisible) {
            CustomDialog(
                title = stringResource(R.string.clear_this_data_title),
                text = stringResource(R.string.clear_this_data_text),
                firstTextButton = stringResource(R.string.cancel),
                secondTextButton = stringResource(R.string.clear),
                enableFirstRequest = true,
                firstRequest = {
                    deleteSetOfContractions.invoke()
                    //      viewModel.deleteSetOfHistory(contractionInSet)
                    dialogClearThisSetVisible = false
                },
                onDismissRequest = { dialogClearThisSetVisible = false },
                changePositionButtons = true
            )
        }

    }
}



