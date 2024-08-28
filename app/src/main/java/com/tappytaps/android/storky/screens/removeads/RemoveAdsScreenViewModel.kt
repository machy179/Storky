package com.tappytaps.android.storky.screens.removeads

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.SkuDetailsParams
import com.tappytaps.android.storky.service.StorkyBillingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class RemoveAdsScreenViewModel @Inject constructor(
    private val billingManager: StorkyBillingManager
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