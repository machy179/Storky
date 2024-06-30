package com.tappytaps.storky.screens.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tappytaps.storky.StopwatchService
import com.tappytaps.storky.model.Contraction
import com.tappytaps.storky.repository.ContractionsRepository
import com.tappytaps.storky.utils.calculateAverageLengthOfContraction
import com.tappytaps.storky.utils.calculateAverageLengthOfIntervalTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: ContractionsRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _listOfContractions =
        MutableStateFlow<List<Contraction>>(emptyList()) //it is list of active Contractions
    val listOfContractions = _listOfContractions.asStateFlow()

    private var _currentContractionLength = mutableStateOf(0)
    val currentContractionLength = _currentContractionLength

    private var _currentLengthBetweenContractions = mutableStateOf(0)
    val currentLengthBetweenContractions = _currentLengthBetweenContractions

    private val _currentTimeDateContraction = mutableStateOf(getCurrentCalendar())
    val currentTimeDateContraction = _currentTimeDateContraction

    private var _dialogShownAutomatically =
        mutableStateOf(false) //if StorkyPopUpDialog was automatically shown
    val dialogShownAutomatically = _dialogShownAutomatically

    var isRunning = false
    private var timerJob: Job? = null
    private var _pauseStopWatch = mutableStateOf(false)
    var pauseStopWatch = _pauseStopWatch

    private var _averageContractionLength = mutableStateOf(0)
    val averageContractionLength = _averageContractionLength

    private var _averageLengthBetweenContractions = mutableStateOf(0)
    val averageLengthBetweenContractions = _averageLengthBetweenContractions


    init {
        getAllActiveContractions()
    }

    private fun getAllActiveContractions() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllActiveContractions().distinctUntilChanged()
                .collect { listOfContractions ->
                    if (listOfContractions.isNullOrEmpty()) {
                        Log.d("Empty", ": Empty list")
                    } else {
                        _listOfContractions.value = listOfContractions
                    }
                    onListOfContractionsLoaded()
                }

        }
    }

    private fun onListOfContractionsLoaded() {
        _currentContractionLength.value = 0
        _currentLengthBetweenContractions.value = 0
        updateAverageTimes(includeCurrentContractionLength = false)
    }

    fun saveCurrentContractionLength() {
        _currentContractionLength.value = _currentLengthBetweenContractions.value

    }


    fun saveContraction() {

        if (isRunning) { //because if is first open, countdowner still does not work - so nothing to save
            viewModelScope.launch {
                try {
                    repository.addContraction(
                        Contraction(
                            lengthOfContraction = _currentContractionLength.value,
                            timeBetweenContractions = _currentLengthBetweenContractions.value,
                            contractionTime = _currentTimeDateContraction.value
                        )
                    )
                    Log.e("Save to database 1:", "1")
                } catch (e: Exception) {
                    Log.e("Save to database error:", e.toString())
                }
            }
        }
    }

    fun updateCurrentTime() {
        _currentTimeDateContraction.value = getCurrentCalendar()
    }

    private fun getCurrentCalendar(): Calendar {
        return Calendar.getInstance()
    }

    fun startStopwatch() {
        updateCurrentTime()
        _currentLengthBetweenContractions.value = 0

        if (!isRunning) {
            isRunning = true
            timerJob = viewModelScope.launch {
                while (isRunning) {
                    delay(1000) // wait for 1 second
                    if (!_pauseStopWatch.value) _currentLengthBetweenContractions.value += 1
                }
            }
        }
    }

    fun stopStopwatch() {
        isRunning = false
        timerJob?.cancel() // Cancel the running coroutine in startStopwatch

    }

    fun newStart() {
        _pauseStopWatch.value = false
        stopStopwatch()
        startStopwatch()
    }

    fun pauseStopWatch() {
        _pauseStopWatch.value = !_pauseStopWatch.value
        _currentLengthBetweenContractions.value =
            -1 //if timer was paused, then timeBetweenContractions is set as -1
    }

    fun newMonitoring() {
        viewModelScope.launch {
            try {
                if (isRunning) {
                    saveContraction()
                    delay(100)
                }
                val contractions = listOfContractions.first() // Collect the latest list
                repository.updateContractionsToHistory(contractions).run {
                    _listOfContractions.value = emptyList()
                    getAllActiveContractions()
                    _dialogShownAutomatically.value = false
                }
                stopStopwatch()
            } catch (e: Exception) {
                Log.e("Save to database error:", e.toString())
            }
        }


    }

    fun updateAverageTimes(includeCurrentContractionLength: Boolean = true) {
        _averageContractionLength.value = calculateAverageLengthOfContraction(
            listOfContractions = _listOfContractions.value,
            currentContractionLength = currentContractionLength.value,
            includeCurrentContractionLength = includeCurrentContractionLength
        )
        _averageLengthBetweenContractions.value =
            calculateAverageLengthOfIntervalTime(listOfContractions = _listOfContractions.value)

    }

    fun setDialogShownAutomaticallyTrue() {
        _dialogShownAutomatically.value = true
    }

    fun deleteContraction(contraction: Contraction) {
        viewModelScope.launch {
            try {
                repository.deleteContraction(contraction)
            } catch (e: Exception) {
            }
        }
    }


    fun startService(context: Context) {
        val intent = Intent(context, StopwatchService::class.java).apply {
            putExtra("currentLengthBetweenContractions", _currentLengthBetweenContractions.value)
            putExtra("pauseStopWatch", _pauseStopWatch.value)
        }
        context.startService(intent)
    }

    fun stopService(context: Context) {
        val intent = Intent(context, StopwatchService::class.java)
        context.stopService(intent)
    }

    fun updateFromService(currentLengthBetweenContractions: Int, pauseStopWatch: Boolean) {
        Log.d("StorkyService:","updateFromService")
        _currentLengthBetweenContractions.value = currentLengthBetweenContractions
        _pauseStopWatch.value = pauseStopWatch
        if (!isRunning && !_pauseStopWatch.value  && _currentLengthBetweenContractions.value != 0) {
            isRunning = true
            timerJob = viewModelScope.launch {
                while (isRunning) {
                    delay(1000) // wait for 1 second
                    _currentLengthBetweenContractions.value += 1
                }
            }
        }
        Log.d("StorkyService:","updateFromService currentLengthBetweenContractions="+currentLengthBetweenContractions.toString())
        Log.d("StorkyService:","updateFromService pauseStopWatch="+pauseStopWatch.toString())
    }


}