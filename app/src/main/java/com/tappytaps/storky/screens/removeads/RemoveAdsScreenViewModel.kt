package com.tappytaps.storky.screens.removeads

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
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoveAdsScreenViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val application: Application
) : ViewModel(), PurchasesUpdatedListener {
    private val billingClient: BillingClient = BillingClient.newBuilder(application)
        .setListener(this)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .build()

    private val _adsDisabled = MutableStateFlow(checkIfAdsDisabled())
    val adsDisabled: StateFlow<Boolean> get() = _adsDisabled

    init {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryPurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to Google Play by calling the startConnection() method.
            }
        })
    }

    fun launchPurchaseFlow(activity: Activity) {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("your_product_id")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()
        Log.d("remove ads Storky","2")
        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                for (productDetails in productDetailsList) {
                    val flowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(
                            listOf(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(productDetails)
                                    .build()
                            )
                        )
                        .build()
                    billingClient.launchBillingFlow(activity, flowParams)
                    Log.d("remove ads Storky","5")
                }
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases)
            Log.d("remove ads Storky","3")
        }
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        Log.d("remove ads Storky","6")
        purchases.forEach { purchase ->
            if (purchase.products.contains("your_product_id") && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                // Disable ads
                Log.d("remove ads Storky","7")
                viewModelScope.launch {
                    with(sharedPreferences.edit()) {
                        putBoolean("ads_disabled", true)
                        apply()
                    }
                    _adsDisabled.value = true
                    Log.d("remove ads Storky","8")
                }
                // Acknowledge the purchase if necessary
                if (!purchase.isAcknowledged) {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            // Purchase acknowledged
                        }
                    }
                }
            }
        }
    }

    private fun queryPurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build()
        ) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                handlePurchases(purchases)
                Log.d("remove ads Storky","4")
            }
        }
    }

    private fun checkIfAdsDisabled(): Boolean {
        return sharedPreferences.getBoolean("ads_disabled", false)
    }
}