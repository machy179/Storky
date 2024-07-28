package com.tappytaps.storky.screens.home

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
import com.tappytaps.storky.StopwatchService
import com.tappytaps.storky.model.Contraction
import com.tappytaps.storky.notification.StorkyNotificationReceiver
import com.tappytaps.storky.repository.ContractionsRepository
import com.tappytaps.storky.service.PdfCreatorAndSender
import com.tappytaps.storky.utils.calculateAverageLengthOfContraction
import com.tappytaps.storky.utils.calculateAverageLengthOfIntervalTime
import com.tappytaps.storky.utils.checkStortkyNotificationAfter5Days
import com.tappytaps.storky.utils.shareSetOfContractionsByEmail
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
    private val application: Application,
    private val pdfCreatorAndSender: PdfCreatorAndSender
) : ViewModel() {

    private val _listOfContractions =
        MutableStateFlow<List<Contraction>>(emptyList()) //it is list of active Contractions
    val listOfContractions = _listOfContractions.asStateFlow()

    private val _currentContractionLength = mutableStateOf(0)
    val currentContractionLength = _currentContractionLength

    private val _currentLengthBetweenContractions = mutableStateOf(0)
    val currentLengthBetweenContractions = _currentLengthBetweenContractions

    private val _currentTimeDateContraction = mutableStateOf(getCurrentCalendar())
    val currentTimeDateContraction = _currentTimeDateContraction

    private val _dialogShownAutomatically =
        mutableStateOf(false) //if StorkyPopUpDialog was automatically shown
    val dialogShownAutomatically = _dialogShownAutomatically

    private val _isRunning = mutableStateOf(false) //because if is first open, stop watch still does not work - so nothing to save
    val isRunning = _isRunning

    private var timerJob: Job? = null
    private val _pauseStopWatch = mutableStateOf(false)
    val pauseStopWatch = _pauseStopWatch

    private val _averageContractionLength = mutableStateOf(0)
    val averageContractionLength = _averageContractionLength

    private val _averageLengthBetweenContractions = mutableStateOf(0)
    val averageLengthBetweenContractions = _averageLengthBetweenContractions

    private val _showContractionlScreen = mutableStateOf(false)
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
                        Log.d("Empty", ": Empty list")
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
                    Log.e("Save to database 1:", "1")
                } catch (e: Exception) {
                    Log.e("Save to database error:", e.toString())
                }
            }
        }
    }

    fun updateCurrentTime() {
        Log.d("delete long press", "6")
        _currentTimeDateContraction.value = getCurrentCalendar()
    }

    private fun getCurrentCalendar(): Calendar {
        return Calendar.getInstance()
    }

    fun startStopwatch() {
        Log.d("delete long press", "5")
        updateCurrentTime()
        _currentLengthBetweenContractions.value = 0

        if (!_isRunning.value) {
            _isRunning.value = true
            timerJob = viewModelScope.launch {
                while (_isRunning.value) {
                    delay(1000) // wait for 1 second
                    Log.d(
                        "delete long press",
                        "_currentLengthBetweenContractions.value: " + _currentLengthBetweenContractions.value.toString()
                    )
                    if (!_pauseStopWatch.value) _currentLengthBetweenContractions.value += 1
                    Log.d(
                        "delete long press",
                        "_currentLengthBetweenContractions.value2: " + _currentLengthBetweenContractions.value.toString()
                    )
                }
            }
        }
    }

    fun stopStopwatch() {
        Log.d("delete long press", "4")
        _isRunning.value = false
        timerJob?.cancel() // Cancel the running coroutine in startStopwatch

    }

    fun newStart() {
        Log.d("delete long press", "3")
        _pauseStopWatch.value = false
        stopStopwatch()
        startStopwatch()
    }

    fun pauseStopWatch() {
        Log.d("delete long press", "2")
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
                    _pauseStopWatch.value = false
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
                    Log.d("delete long press", "1")
                }


            } catch (e: Exception) {
            }
        }
    }

    fun setButtonStopContractionAlreadyPresed(value: Boolean) {
        buttonStopContractionAlreadyPresed = value
    }

    fun deleteCurrentContraction() { //function for dele current Contraction from ContractionScreen
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
        Log.d("StorkyService:", "startService_from_HomeScreenViewModel")
        val intent = Intent(context, StopwatchService::class.java).apply {
            putExtra("currentLengthBetweenContractions", _currentLengthBetweenContractions.value)
            putExtra("showContractionlScreen", _showContractionlScreen.value)
            putExtra("pauseStopWatch", _pauseStopWatch.value)
            putExtra("currentContractionLength", _currentContractionLength.value)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // context.startForegroundService(intent)
            ContextCompat.startForegroundService(context, intent)
        } else {
            context.startService(intent)
        }
        //    context.startService(intent)
    }

    fun stopService(context: Context) {
        val intent = Intent(context, StopwatchService::class.java)

        context.stopService(intent)
    }

    fun updateFromService(
        currentLengthBetweenContractions: Int,
        pauseStopWatch: Boolean,
        showContractionlScreen: Boolean,
        currentContractionLength: Int,
    ) {
        Log.d("StorkyService:", "updateFromService")
        _currentLengthBetweenContractions.value = currentLengthBetweenContractions
        _pauseStopWatch.value = pauseStopWatch
        _showContractionlScreen.value = showContractionlScreen
        _currentContractionLength.value = currentContractionLength
        Log.d(
            "StorkyService:",
            "updateFromService _currentLengthBetweenContractions.value" + _currentLengthBetweenContractions.value.toString()
        )
        if (!_isRunning.value && !_pauseStopWatch.value && _currentLengthBetweenContractions.value != 0) {
            Log.d("StorkyService:", "updateFromService_startSotopWatch")

            _isRunning.value = true
            timerJob = viewModelScope.launch {
                while (_isRunning.value) {
                    delay(1000) // wait for 1 second
                    _currentLengthBetweenContractions.value += 1
                }
            }
        }
        Log.d(
            "StorkyService:",
            "updateFromService currentLengthBetweenContractions=" + currentLengthBetweenContractions.toString()
        )
        Log.d("StorkyService:", "updateFromService pauseStopWatch=" + pauseStopWatch.toString())
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
                            System.currentTimeMillis() + 1 * 60 * 1000 // 1 minuty v milisekundách*/

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)

            Toast.makeText(application, "Alarm set for 5 days from now", Toast.LENGTH_LONG).show()
        }

    }

    fun deleteActualSet(set: Int) {
        Log.d("Storky delete:", "clicked")
        Log.d("Actual contraction deleteSetOfHistory: ", "deleteSetOfHistory")
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
        currentTimeDateContraction: Calendar
    ) {
        shareSetOfContractionsByEmail(context = context,
            contractionInSet = contractionInSet,
            listOfContractions = _listOfContractions,
            pdfCreatorAndSender = pdfCreatorAndSender,
            viewModel = this,
            currentContractionLength = currentContractionLength,
            currentTimeDateContraction = currentTimeDateContraction
            )



    }


}