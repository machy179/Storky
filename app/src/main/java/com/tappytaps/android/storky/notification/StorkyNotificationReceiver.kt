package com.tappytaps.android.storky.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tappytaps.android.storky.MainActivity
import com.tappytaps.android.storky.R


const val notificationID = 2
const val channelID = "STORKY_POPUP_AFTER_5DAYS_CHANNEL"
class StorkyNotificationReceiver : BroadcastReceiver() { //receiver for notification after 5 days

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
        ExperimentalComposeUiApi::class
    )
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("screen", "TryBibinoScreen")
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.notification_after_5days_title))
            .setContentText(context.getString(R.string.notification_after_5days_text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)

        // Create Notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelID, "Storky Notification Channel", NotificationManager.IMPORTANCE_HIGH).apply {
                setShowBadge(false) // Ensure the notification does not show a badge on the app icon
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationID, notificationBuilder.build())
    }
}