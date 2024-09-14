package com.tappytaps.android.storky.screens.home

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tappytaps.android.storky.StopwatchService
import com.tappytaps.android.storky.model.Contraction
import com.tappytaps.android.storky.model.StorkyStopwatchState
import com.tappytaps.android.storky.notification.StorkyNotificationReceiver
import com.tappytaps.android.storky.repository.ContractionsRepository
import com.tappytaps.android.storky.service.PdfCreatorAndSender
import com.tappytaps.android.storky.utils.calculateAverageLengthOfContraction
import com.tappytaps.android.storky.utils.calculateAverageLengthOfIntervalTime
import com.tappytaps.android.storky.utils.checkStortkyNotificationAfter5Days
import com.tappytaps.android.storky.utils.shareSetOfContractionsByEmail
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
open class HomeScreenViewModel @Inject constructor(
    private val repository: ContractionsRepository,
    private val application: Application,
    private val pdfCreatorAndSender: PdfCreatorAndSender,
    private val storkyStopwatchState: StorkyStopwatchState,
) : ViewModel() {

    private val _listOfContractions =
        MutableStateFlow<List<Contraction>>(emptyList()) //it is list of active Contractions
    val listOfContractions = _listOfContractions.asStateFlow()

    private val _currentContractionLength = storkyStopwatchState.currentContractionLength
    val currentContractionLength = _currentContractionLength

    private val _currentLengthBetweenContractions =
        storkyStopwatchState.currentLengthBetweenContractions
    val currentLengthBetweenContractions = _currentLengthBetweenContractions

    private val _currentTimeDateContraction = mutableStateOf(getCurrentCalendar())
    val currentTimeDateContraction = _currentTimeDateContraction

    private val _dialogShownAutomatically =
        mutableStateOf(false) //if StorkyPopUpDialog was automatically shown
    val dialogShownAutomatically = _dialogShownAutomatically

    private val _isRunning =
        storkyStopwatchState.isRunning //because if is first open, stop watch still does not work - so nothing to save
    val isRunning = _isRunning


    private var timerJob: Job? = null

    private val _pauseStopWatch = storkyStopwatchState.pauseStopWatch
    val pauseStopWatch = _pauseStopWatch

    private val _averageContractionLength = mutableStateOf(0)
    val averageContractionLength = _averageContractionLength

    private val _averageLengthBetweenContractions = mutableStateOf(0)
    val averageLengthBetweenContractions = _averageLengthBetweenContractions

    private val _showContractionlScreen = storkyStopwatchState.showContractionlScreen
    val showContractionlScreen = _showContractionlScreen


    private var buttonStopContractionAlreadyPresed =
        false //for delete Contraction, if this is true, we have to delete last Contraction in _listOfContractions
    // in deleteCurrentContraction() function and set up atributes from last Contraction to _currentContractionLength, _currentLengthBetweenContractions and _currentTimeDateContraction
    //if this is false, we cann not load current data from last Contraction (stop watch was not running, because of last Contraction was stopped or new open app)

    init {
        getAllActiveContractions()
    }

    fun setShowContractionlScreen(value: Boolean) {
        _showContractionlScreen.value = value
    }

    private fun getAllActiveContractions() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllActiveContractions().distinctUntilChanged()
                .collect { listOfContractions ->
                    if (listOfContractions.isEmpty()) {
                    } else {
                        _listOfContractions.value = listOfContractions
                    }
                    onListOfContractionsLoaded()
                }

        }
    }

    private fun onListOfContractionsLoaded() {
        //    _currentContractionLength.value = 0
        //   _currentLengthBetweenContractions.value = 0
        updateAverageTimes(includeCurrentContractionLength = false)
    }

    fun saveCurrentContractionLength() {
        _currentContractionLength.value = _currentLengthBetweenContractions.value

    }


    fun saveContraction() {

        if (_isRunning.value) { //because if is first open, countdowner still does not work - so nothing to save
            viewModelScope.launch {
                try {
                    repository.addContraction(
                        Contraction(
                            lengthOfContraction = _currentContractionLength.value,
                            timeBetweenContractions = _currentLengthBetweenContractions.value,
                            contractionTime = _currentTimeDateContraction.value
                        )
                    )
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

        if (!_isRunning.value) {
            _isRunning.value = true
            timerJob = viewModelScope.launch {
                while (_isRunning.value) {
                    delay(1000)
                    if (!_pauseStopWatch.value) _currentLengthBetweenContractions.value += 1
                }
            }
        }
    }

    fun stopStopwatch() {
        _isRunning.value = false
        timerJob?.cancel() // Cancel the running coroutine in startStopwatch

    }

    fun newStart() {
        _pauseStopWatch.value = false
        stopStopwatch()
        startStopwatch()
    }

    fun pauseStopWatch() {
        _pauseStopWatch.value = true //
        _currentLengthBetweenContractions.value =
            -1 //if timer was paused, then timeBetweenContractions is set as -1
        buttonStopContractionAlreadyPresed = false
    }

    fun newMonitoring() {
        viewModelScope.launch {
            try {
                if (_isRunning.value) {
                    saveContraction()
                    delay(100)
                }
                val contractions = listOfContractions.first() // Collect the latest list
                repository.updateContractionsToHistory(contractions).run {
                    _listOfContractions.value = emptyList()
                    getAllActiveContractions()
                    _dialogShownAutomatically.value = false
                    _currentContractionLength.value = 0
                    _currentLengthBetweenContractions.value = 0
                    _pauseStopWatch.value = true
                }
                stopStopwatch()
            } catch (e: Exception) {
                Log.e("Save to database error:", e.toString())
            }
            buttonStopContractionAlreadyPresed = false
        }


    }

    fun updateAverageTimes(includeCurrentContractionLength: Boolean = true) {
        val oneHourAgo = Calendar.getInstance().apply {
            add(Calendar.HOUR, -1)
        }
        val listOfContractionsLastHour =
            _listOfContractions.value.filter { it.contractionTime.after(oneHourAgo) }

        _averageContractionLength.value = calculateAverageLengthOfContraction(
            listOfContractions = listOfContractionsLastHour,
            currentContractionLength = currentContractionLength.value,
            includeCurrentContractionLength = includeCurrentContractionLength
        )
        _averageLengthBetweenContractions.value =
            calculateAverageLengthOfIntervalTime(listOfContractions = listOfContractionsLastHour)

    }

    fun setDialogShownAutomaticallyTrue() {
        _dialogShownAutomatically.value = true
    }

    fun deleteContraction(contraction: Contraction) { //function for dele Contraction (row) by long press
        viewModelScope.launch {
            try {
                if (_listOfContractions.value.size == 1) { //if it is last Contraction in list, it is necessary to delete whole list, becacuse
                    //if we try delete just one Contraction, there was layout bugs - this Contraction was still seen, although list was empty
                    repository.deleteAllActiveContractions()
                    _listOfContractions.value = emptyList()
                    getAllActiveContractions()
                } else {
                    repository.deleteContraction(contraction)
                }


            } catch (e: Exception) {
            }
        }
    }

    fun setButtonStopContractionAlreadyPresed(value: Boolean) {
        buttonStopContractionAlreadyPresed = value
    }

    fun deleteCurrentContraction() { //function for delete current Contraction from ContractionScreen
        viewModelScope.launch {
            try {
                if (_listOfContractions.value.size > 0 && buttonStopContractionAlreadyPresed) {
                    val lastContraction = _listOfContractions.value.first()

                    val copiedTimeBetweenContractions = lastContraction.timeBetweenContractions
                    val copiedLengthOfContraction = lastContraction.lengthOfContraction
                    val copiedContractionTime = lastContraction.contractionTime
                    val copiedLastContractionId = lastContraction.id.toString()
                    val copiedCurrentLengthBetweenContractions =
                        _currentLengthBetweenContractions.value
                    repository.deleteContractionById(copiedLastContractionId)

                    _currentLengthBetweenContractions.value =
                        copiedTimeBetweenContractions + copiedCurrentLengthBetweenContractions
                    _currentContractionLength.value = copiedLengthOfContraction
                    _currentTimeDateContraction.value = copiedContractionTime


                    if (_listOfContractions.value.size == 1) {
                        repository.deleteContractionById(copiedLastContractionId)
                        _listOfContractions.value = emptyList()
                        getAllActiveContractions()
                    } else {
                        repository.deleteContractionById(copiedLastContractionId)
                    }

                } else {
                    stopStopwatch()
                }


            } catch (e: Exception) {
            }
        }
    }


    fun startService(context: Context) {
        val intent = Intent(context, StopwatchService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intent)
        } else {
            context.startService(intent)
        }
        timerJob?.cancel()
    }

    fun stopService(context: Context) {
        val intent = Intent(context, StopwatchService::class.java)
        context.stopService(intent)
    }


    fun checkIfItIsFromService() {
         //it is necessary to check if the user is back from the service (background app):
        if ((timerJob == null || timerJob!!.isCancelled) && !_pauseStopWatch.value && _currentLengthBetweenContractions.value != 0) {
            //the user returns to the application from the serivice and it is necessary to run timerJob:
            _isRunning.value = true
            timerJob = viewModelScope.launch {
                while (_isRunning.value) {
                    delay(1000) // wait for 1 second
                    if (!_pauseStopWatch.value) _currentLengthBetweenContractions.value += 1
                }
            }

            if(_currentLengthBetweenContractions.value > 1200 && _showContractionlScreen.value == false) {
                //if the application goes from the background to the foreground, the measurement is running, there is no contraction and the measurement is longer than 20 minutes, then the measurement is paused
                pauseStopWatch()
            }
       }
    }


    fun setAlarmAfter5Days() {
        // Schedule the notification to appear after 5 days
        if (checkStortkyNotificationAfter5Days(
                listOfContractions = _listOfContractions.value,
                currentContractionLength = _currentContractionLength.value
            )
        ) {
            val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(application, StorkyNotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                application,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val triggerTime = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 5) //after 5 days
            }.timeInMillis

            /*            val timeInMillis =
                            System.currentTimeMillis() + 1 * 60 * 1000 // just for testitng: 1 minute in milisec*/

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)

            Toast.makeText(application, "Alarm set for 5 days from now", Toast.LENGTH_LONG).show()
        }

    }

    fun deleteActualSet(set: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteContractionsBySet(set = set).run {
                delay(100)
                _listOfContractions.value = emptyList<Contraction>()
                getAllActiveContractions()
                _dialogShownAutomatically.value = false
                stopStopwatch()
                buttonStopContractionAlreadyPresed = false
            }


        }

    }

    fun shareSetsOfActualContractionsByEmail(
        context: Context,
        contractionInSet: Int,
        currentContractionLength: Int,
        currentTimeDateContraction: Calendar,
    ) {
        shareSetOfContractionsByEmail(
            context = context,
            contractionInSet = contractionInSet,
            listOfContractions = _listOfContractions,
            pdfCreatorAndSender = pdfCreatorAndSender,
            viewModel = this,
            currentContractionLength = currentContractionLength,
            currentTimeDateContraction = currentTimeDateContraction
        )


    }


}