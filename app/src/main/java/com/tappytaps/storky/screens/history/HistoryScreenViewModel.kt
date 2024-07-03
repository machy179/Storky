package com.tappytaps.storky.screens.history

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tappytaps.storky.model.Contraction
import com.tappytaps.storky.repository.ContractionsRepository
import com.tappytaps.storky.service.PdfCreatorAndSender
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.io.File
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
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHistoryContractionsBySet(set = set).run {
                _listOfContractionsHistory.value = emptyList<Contraction>()
                getAllHistoryContractions()
            }

        }

    }


    fun shareEmailHistory(context: Context, contractionInSet: Int) {

        val filteredContractionList: List<Contraction> = _listOfContractionsHistory
            .value // Get the current value of the MutableStateFlow
            .filter { it.in_set == contractionInSet } // Filter the list
            .toList() // Convert to an immutable List
        var pdfFile: File
        viewModelScope.launch {
            pdfFile = pdfCreatorAndSender.convertToPdf(
                filteredContractionList = filteredContractionList,
                context = context
            )
            pdfCreatorAndSender.sendEmailWithAttachment(
                file = pdfFile,
                context = context
            )
        }


    }

    fun deleteContraction(contraction: Contraction) {
        viewModelScope.launch {
            try {

                if(_listOfContractionsHistory.value.filter { it.in_set == contraction.in_set }.size == 1)  {
                    deleteSetOfHistory(set = contraction.in_set )
                } else {
                    repository.deleteContraction(contraction)
                }
            } catch (e: Exception) {
            }
        }
    }


}