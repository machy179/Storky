package com.tappytaps.android.storky.model

import androidx.compose.runtime.mutableStateOf

class StorkyStopwatchState() { //it will be singleton in Hilt DI, because it is necessary to have ONE state of these attributes in Activity and Service
    private val _currentContractionLength = mutableStateOf(0)
    val currentContractionLength = _currentContractionLength

    private val _currentLengthBetweenContractions = mutableStateOf(0)
    val currentLengthBetweenContractions = _currentLengthBetweenContractions

    private val _pauseStopWatch = mutableStateOf(false)
    val pauseStopWatch = _pauseStopWatch

    private val _showContractionlScreen = mutableStateOf(false)
    val showContractionlScreen = _showContractionlScreen

    private val _isRunning = mutableStateOf(false)
    val isRunning = _isRunning
}