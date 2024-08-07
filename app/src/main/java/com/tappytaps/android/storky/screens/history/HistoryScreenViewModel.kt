package com.tappytaps.android.storky.screens.history

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tappytaps.android.storky.model.Contraction
import com.tappytaps.android.storky.repository.ContractionsRepository
import com.tappytaps.android.storky.service.PdfCreatorAndSender
import com.tappytaps.android.storky.utils.shareSetOfContractionsByEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val repository: ContractionsRepository,
    private val pdfCreatorAndSender: PdfCreatorAndSender,
) : ViewModel() {

    private val _listOfContractionsHistory = MutableStateFlow<List<Contraction>>(emptyList())
    val listOfContractionsHistory = _listOfContractionsHistory.asStateFlow()

    init {
        getAllHistoryContractions()
    }


    private fun getAllHistoryContractions() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllHistoryContractions().distinctUntilChanged()
                .collect { listOfContractionsHistory ->
                    if (listOfContractionsHistory.isNullOrEmpty()) {
                    } else {
                        _listOfContractionsHistory.value = listOfContractionsHistory
                    }

                }

        }
    }

    fun deleteAllHistory() {
        Log.d("Storky delete:", "clicked")
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllHistoryContractions().run {
                _listOfContractionsHistory.value = emptyList<Contraction>()
                getAllHistoryContractions()
                Log.d("Storky delete:", "done")
            }

        }
    }

    fun deleteSetOfHistory(set: Int) {
        Log.d("Storky delete:", "clicked")
        Log.d("Actual contraction deleteSetOfHistory: ", "deleteSetOfHistory")
        viewModelScope.launch(Dispatchers.IO) {
/*            repository.deleteHistoryContractionsBySet(set = set).run {
                _listOfContractionsHistory.value = emptyList<Contraction>()
                getAllHistoryContractions()
            }*/
            repository.deleteContractionsBySet(set = set).run {
                _listOfContractionsHistory.value = emptyList<Contraction>()
                getAllHistoryContractions()
            }


        }

    }


    fun shareSetsOfHistoryContractionsByEmail(context: Context, contractionInSet: Int) {
        shareSetOfContractionsByEmail(
            context = context,
            contractionInSet = contractionInSet,
            listOfContractions = _listOfContractionsHistory,
            viewModel = this,
            pdfCreatorAndSender = pdfCreatorAndSender
        )
    }

    fun deleteContraction(contraction: Contraction) {
        viewModelScope.launch {
            try {

                if (_listOfContractionsHistory.value.filter { it.in_set == contraction.in_set }.size == 1) {
                    //if it is last Contraction in set, it is necessary to delete whole set, becacuse
                    //if we try delete just one Contraction, there was layout bugs - this Contraction was still seen, although set was empty
                    deleteSetOfHistory(set = contraction.in_set)
                } else {
                    repository.deleteContraction(contraction)
                }
            } catch (e: Exception) {
            }
        }
    }


}