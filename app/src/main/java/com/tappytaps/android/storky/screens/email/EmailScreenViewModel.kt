package com.tappytaps.android.storky.screens.email

import androidx.lifecycle.ViewModel
import com.tappytaps.android.storky.repository.EmailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class EmailScreenViewModel @Inject constructor(
    private val repository: EmailRepository
): ViewModel() {


        open fun sendEmail(email: String) {
            repository.sendEmail(email)
        }
}