package com.tappytaps.storky.screens.email

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tappytaps.storky.repository.EmailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailScreenViewModel @Inject constructor(
    private val repository: EmailRepository
): ViewModel() {


        fun sendEmail(email: String) {
            repository.sendEmail(email)
        }
}