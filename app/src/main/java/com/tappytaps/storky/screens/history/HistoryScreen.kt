package com.tappytaps.storky.screens.history

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
import androidx.compose.foundation.rememberScrollState
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
import com.tappytaps.storky.components.ContractionRowByItems
import com.tappytaps.storky.components.CustomDialog
import com.tappytaps.storky.components.StorkyAppBar
import com.tappytaps.storky.components.StorkyDropMenuItem
import com.tappytaps.storky.components.imageTitleContentText
import com.tappytaps.storky.model.Contraction
import com.tappytaps.storky.navigation.StorkyScreens
import com.tappytaps.storky.utils.getDateInHistory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryScreenViewModel,
    listOfContractionsHistory: List<Contraction>
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val scrollState = rememberScrollState()
    var dialogClearAllDataVisible by remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            StorkyAppBar(
                titleNameOfScreen = stringResource(R.string.history),
                backgroundColor = MaterialTheme.colorScheme.background,
                deleteIconVisible = true,
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


        if (listOfContractionsHistory.isNullOrEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center),//.padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally, // Center images horizontally
                    verticalArrangement = Arrangement.Center
                ) {

                    var imageResId: Int = R.drawable.empty
                    var titleResId: Int = R.string.history_title
                    var textResId: Int = R.string.history_text


                    imageTitleContentText(
                        imageResId = imageResId,
                        titleResId = titleResId,
                        textResId = textResId,
                        bottomSpace = true
                    )

                }
            }
        } else {
            val listState = rememberLazyListState()
            LaunchedEffect(key1 = Unit) {
                listState.animateScrollToItem(index = 0) //it is for roll lazycolumn to top
            }

            val ids = mutableListOf<Long>() //to find, which Contractions are first in sets
            var previousSet: Int? = null

            for (contraction in listOfContractionsHistory) {
                if (previousSet == null || contraction.in_set != previousSet) {
                    ids.add(contraction.id)
                }
                previousSet = contraction.in_set
            }
            var SizeListOfContractionsHistory = listOfContractionsHistory.size

            LazyColumn(
                modifier = Modifier.padding(paddingValues),//.verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top // Start items from the top
            ) {
                itemsIndexed(listOfContractionsHistory,
                    key = { index, contraction -> contraction.id }
                ) { index, contraction ->
                    if (contraction.id in ids) {
                        Spacer(modifier = Modifier.height(24.dp))
                        barSetsContractions(
                            navController = navController,
                            listOfContractionsHistory = listOfContractionsHistory,
                            contraction = contraction,
                            viewModel = viewModel,
                            contractionInSet = contraction.in_set)
                    }
                    val reversedIndex = SizeListOfContractionsHistory - index
                    ContractionRowByItems(
                        contraction = contraction,
                        numberOfContraction = reversedIndex,
                        onLongClick = { viewModel.deleteContraction(contraction) }
                    )
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
                        viewModel.deleteAllHistory()
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
private fun barSetsContractions(
    navController: NavController,
    listOfContractionsHistory: List<Contraction>,
    contraction: Contraction,
    viewModel: HistoryScreenViewModel,
    contractionInSet: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(), // Ensure the Row takes up the full width
        horizontalArrangement = Arrangement.SpaceBetween // Space out items to opposite ends
    ) {
        var menuExpanded by remember { mutableStateOf(false) }
        var dialogClearThisSetVisible by remember { mutableStateOf(false) }
        val context = LocalContext.current
        Text(
            text = getDateInHistory(
                listOfContractionsHistory.filter { it.in_set == contraction.in_set }
                    .last().contractionTime,
                listOfContractionsHistory.filter { it.in_set == contraction.in_set }
                    .first().contractionTime
            ),
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
                    onClick = { menuExpanded = false
                        viewModel.shareEmailHistory(context = context,
                            contractionInSet = contractionInSet)
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
                    viewModel.deleteSetOfHistory(contraction.in_set)
                    dialogClearThisSetVisible = false
                },
                onDismissRequest = { dialogClearThisSetVisible = false },
                changePositionButtons = true
            )
        }

    }
}
