package com.tappytaps.storky.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.tappytaps.storky.BuildConfig
import com.tappytaps.storky.utils.Constants

@Composable
fun StorkyNativeAdView() {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    val adUnitIdNative = if (BuildConfig.DEBUG) Constants.AD_UNIT_ID_NATIVE_TEST else Constants.AD_UNIT_ID_NATIVE_TAPPYTAPS
    LaunchedEffect(Unit) {
        val adLoader = AdLoader.Builder(context, adUnitIdNative)
            .forNativeAd { ad: NativeAd ->
                nativeAd = ad
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e("NativeAd", "Ad failed to load: ${adError.message}")
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    nativeAd?.let { ad ->

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLow, shape = RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(start = 14.dp, end = 14.dp, top = 26.dp, bottom = 14.dp)
            ) {
                //           Spacer(modifier = Modifier.width(14.dp))

                ad.images.firstOrNull()?.drawable?.let {
                    Image(
                        painter = rememberAsyncImagePainter(model = it),
                        contentDescription = null,
                        modifier = Modifier
                            .size(91.dp)
                            .padding(bottom = 12.dp)
                            .align(Alignment.CenterVertically),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        ad.icon?.let {
                            Image(
                                painter = rememberAsyncImagePainter(model = it.drawable),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                                    .padding(end = 8.dp)
                            )
                        }
                        Text(
                            text = ad.headline ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                        )
                    }

                    ad.body?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    ad.callToAction?.let {
                        UniversalButton(
                            text = it,
                            onClick = { },
                            disableInsetNavigationBarPadding = true
                        )
                    }
                }
            }
        }

    }
}


