package com.tappytaps.storky.screens.splash

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel  @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {


    fun checkFirstRun(): Boolean {
        val isFirstRun = sharedPreferences.getBoolean("is_first_run", true)
        if (isFirstRun) {
            sharedPreferences.edit().putBoolean("is_first_run", false).apply()
        }
        return isFirstRun
    }



}
