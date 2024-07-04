package com.tappytaps.storky.screens.settings

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tappytaps.storky.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    private val _lengthOfContraction = mutableStateOf(0)
    val lengthOfContraction = _lengthOfContraction

    private val _lengthOfInterval = mutableStateOf(0)
    val lengthOfInterval = _lengthOfInterval

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        _lengthOfContraction.value = sharedPreferences.getInt(
            "lengthOfContraction",
            Constants.DEFAULT_CONTRACTION_LENGTH
        )
        _lengthOfInterval.value = sharedPreferences.getInt(
            "lengthOfInterval",
            Constants.DEFAULT_INTERVAL_LENGTH
        )
    }

    fun setLengthOfContraction(length: Int) {
        _lengthOfContraction.value = length
        savePreference("lengthOfContraction", length)
    }

    fun setLengthOfInterval(length: Int) {
        _lengthOfInterval.value = length
        savePreference("lengthOfInterval", length)
    }

    private fun savePreference(key: String, value: Int) {
        viewModelScope.launch {
            sharedPreferences.edit().putInt(key, value).apply()
        }
    }
}