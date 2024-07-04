package com.tappytaps.storky.screens.email

import androidx.lifecycle.ViewModel
import com.tappytaps.storky.repository.EmailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmailScreenViewModel @Inject constructor(
    private val repository: EmailRepository
): ViewModel() {


        fun sendEmail(email: String) {
            repository.sendEmail(email)
        }
}