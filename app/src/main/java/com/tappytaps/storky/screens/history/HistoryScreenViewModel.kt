package com.tappytaps.storky.screens.history

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tappytaps.storky.model.Contraction
import com.tappytaps.storky.repository.ContractionsRepository
import com.tappytaps.storky.utils.getHtmlContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val repository: ContractionsRepository
): ViewModel() {

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
            //    _listOfContractionsHistory.value = emptyList<Contraction>()
                getAllHistoryContractions()
            }

        }

    }


    fun shareEmailHistory(context: Context, contractionInSet: Int) {
        val filteredContractionList: List<Contraction> = _listOfContractionsHistory
            .value // Get the current value of the MutableStateFlow
            .filter { it.in_set == contractionInSet } // Filter the list
            .toList() // Convert to an immutable List

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            data = Uri.parse("mailto:") // Set data as empty email to open email composer
    //        putExtra(Intent.EXTRA_EMAIL, "machy79@seznam.cz") // Set recipient email
            putExtra(Intent.EXTRA_SUBJECT, "Storky indicator contractions") // Set email subject
        }

// Create styled HTML content with table

/*
        val emailBody2 = """
            <h1>Welcome to Our Service</h1>
            <p>Dear User,</p>
            <p>Thank you for signing up for our service. We are excited to have you on board.</p>
            <p>Best regards,<br/>Your Company</p>
            <a href=“mailto:name@test.com”>name@test.com</a>
        """.trimIndent()*/

/*
        val html =
            "<!DOCTYPE html><html><body><a href=\"http://www.w3schools.com\" target=\"_blank\">Visit W3Schools.com!</a>" + "<p>If you set the target attribute to \"_blank\", the link will open in a new browser window/tab.</p></body></html>"
*/


// Set HTML content as HTML text
       // emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, Html.fromHtml(msgHtml, Html.FROM_HTML_MODE_LEGACY))
        emailIntent.putExtra(Intent.EXTRA_TEXT, HtmlCompat.fromHtml(getHtmlContent(listOfContractions = filteredContractionList), HtmlCompat.FROM_HTML_MODE_LEGACY))

// Set intent type as HTML
        emailIntent.type = "text/html"
  //      emailIntent.setType("message/rfc822")

        // Check if there are email apps available
        if (emailIntent.resolveActivity(context.packageManager) != null) {
            // Start email activity with chooser
            context.startActivity(Intent.createChooser(emailIntent, "Choose an email client"))
        } else {
            // No email apps available
            Toast.makeText(context, "No email apps found", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteContraction(contraction: Contraction) {
        viewModelScope.launch {
            try {
                repository.deleteContraction(contraction)
            } catch (e: Exception) {
            }
        }
    }


}