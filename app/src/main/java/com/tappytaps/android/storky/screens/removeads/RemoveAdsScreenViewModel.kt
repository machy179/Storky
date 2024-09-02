package com.tappytaps.android.storky.screens.removeads

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.tappytaps.android.storky.service.StorkyBillingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
open class RemoveAdsScreenViewModel @Inject constructor(
    private val billingManager: StorkyBillingManager,
) : ViewModel() {

    val adsDisabled: StateFlow<Boolean> = billingManager.adsDisabled
    val purchaseInProgress = billingManager.isPurchaseInProgress

    fun startPurchase(activity: Activity) {
        billingManager.startPurchase(activity)
    }

    override fun onCleared() {
        super.onCleared()
        billingManager.endConnection()
    }


}