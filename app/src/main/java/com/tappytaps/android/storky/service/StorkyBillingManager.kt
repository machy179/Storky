package com.tappytaps.android.storky.service

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.tappytaps.android.storky.utils.Constants.BILLING_ID_BUY_APP_TAPPYTAPS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class StorkyBillingManager @Inject constructor(
    application: Application,
    private val sharedPreferences: SharedPreferences,
) {

    private val billingClient: BillingClient = BillingClient.newBuilder(application)
        .setListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                purchases.forEach { purchase ->
                    handlePurchase(purchase)
                }
            }
            // Reset isPurchaseInProgress when purchase flow finishes
            _isPurchaseInProgress.value = false
        }
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .build()

    private val _adsDisabled = MutableStateFlow(true)
    val adsDisabled: StateFlow<Boolean> = _adsDisabled.asStateFlow()


    private var _isPurchaseInProgress = mutableStateOf(false)
    val isPurchaseInProgress = _isPurchaseInProgress

    init {
        _adsDisabled.value = sharedPreferences.getBoolean("ads_disabled", false)
        startConnection()
    }

    private fun startConnection() {
        //because of bug on Google Play - more than 999 connection attempts
        if (billingClient.isReady) {
            // Billing client is already connected, no need to reconnect
            return
        }
        billingClient.startConnection(object : BillingClientStateListener {
            //Create new scratch file from selection
            override fun onBillingServiceDisconnected() {
                // Add a delay or backoff strategy before attempting to reconnect
                Handler(Looper.getMainLooper()).postDelayed({
                    startConnection()
                }, 2000) // Retry after 2 seconds
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryPurchases()
                }
            }
        })
    }

    private fun queryPurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val purchased =
                    purchases.any { it.purchaseState == Purchase.PurchaseState.PURCHASED }

                if (purchased != _adsDisabled.value) {//in shared preferences the value false is stored as not purchased, but Google verified that the gmail account has already purchased it
                    //this will be a previously purchased application, uninstall and then reinstall, so it is also necessary to set it in the app that the user bought it
                    _adsDisabled.value = !_adsDisabled.value
                    savePurchaseStateToPreferences(adsRemoved = _adsDisabled.value)
                }

                if (true) { //IMPORTANT, it is just for debugging billing - here is purchase deactivated after new open app - so it can be testing purchase again
                    purchases.forEach { purchase ->
                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                            val consumeParams = ConsumeParams.newBuilder()
                                .setPurchaseToken(purchase.purchaseToken)
                                .build()

                            billingClient.consumeAsync(consumeParams) { consumeResult, purchaseToken ->
                                if (consumeResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                    Log.d("BillingManager", "Purchase consumed: $purchaseToken")
                                    savePurchaseStateToPreferences(adsRemoved = false)
                                } else {
                                    Log.e(
                                        "BillingManager",
                                        "Failed to consume purchase: ${consumeResult.debugMessage}"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun startPurchase(activity: Activity) {
        if (_isPurchaseInProgress.value) {
            Log.d("BillingManager", "Purchase already in progress, ignoring subsequent request.")
            return
        }

        _isPurchaseInProgress.value = true

        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(BILLING_ID_BUY_APP_TAPPYTAPS)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList != null) {
                val flowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(
                        listOf(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetailsList[0])
                                .build()
                        )
                    )
                    .build()
                billingClient.launchBillingFlow(activity, flowParams)
            } else {
                // Reset the flag if queryProductDetailsAsync fails
                _isPurchaseInProgress.value = false
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
            val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

            billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    _adsDisabled.value = true
                    savePurchaseStateToPreferences(adsRemoved = true)
                }
            }
        }
    }

    private fun savePurchaseStateToPreferences(adsRemoved: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean("ads_disabled", adsRemoved)
            apply()
        }
    }

    fun endConnection() {
        billingClient.endConnection()
    }
}