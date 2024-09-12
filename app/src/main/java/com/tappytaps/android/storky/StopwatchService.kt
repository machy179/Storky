package com.tappytaps.android.storky

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.tappytaps.android.storky.model.StorkyStopwatchState
import com.tappytaps.android.storky.utils.convertSecondsToTimeString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopwatchService : Service() {
    private var wakeLock: PowerManager.WakeLock? =
        null //so that the service does not fall into Doze Mode, it is necessary to prevent this here,
    // plus give this to manifestu  <uses-permission android:name="android.permission.WAKE_LOCK" />
    //and put the wakeLock attribute in the code as I put it...viz https://robertohuertas.com/2019/06/29/android_foreground_services/

    @Inject
    lateinit var storkyStopwatchState: StorkyStopwatchState

    private var serviceJob: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.Default)

    private var isRunning = false
    private lateinit var currentLengthBetweenContractions: MutableState<Int>
    private lateinit var pauseStopWatch: MutableState<Boolean>
    private lateinit var showContractionlScreen: MutableState<Boolean>

    private var notification: Notification? = null
    private var builder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null

    private val NOTIFICATION_ID = 1

    override fun onCreate() {
        super.onCreate()
        currentLengthBetweenContractions = storkyStopwatchState.currentLengthBetweenContractions
        showContractionlScreen = storkyStopwatchState.showContractionlScreen
        pauseStopWatch = storkyStopwatchState.pauseStopWatch
        try {
            startForegroundService()
        } catch (e: Exception) {
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) { //if user kill the Activity, thi metod will be called - so this metod viwll destroy this service
        super.onTaskRemoved(rootIntent)
        storkyStopwatchState.currentContractionLength.value = 0
        storkyStopwatchState.currentLengthBetweenContractions.value = 0
        storkyStopwatchState.showContractionlScreen.value = false
        storkyStopwatchState.pauseStopWatch.value = true
        storkyStopwatchState.isRunning.value = false
        stopSelf()
        Log.d("Storky destroy", "onTaskRemoved: ")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        isRunning = true
        startStopwatch()

        //because of not to Doze Modu
        // we need this lock so our service gets not affected by Doze Mode
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire()
                }
            }
        //it's here so that this service doesn't kill itself after about 1 minute after turning the phone on - but I still had to add the above, because it didn't kill itself, but gradually went into Doze Mode
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopStopwatch()

        // We need this release because of Doze Mode
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
        } catch (e: Exception) {
        }
        notificationManager?.cancel(NOTIFICATION_ID)

    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForegroundService() {
        val notificationChannelId = "STORKY_SERVICE_CHANNEL"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Storky Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false) // Ensure the notification does not show a badge on the app icon
            }
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }

        notification = buildNotification()
        startForeground(NOTIFICATION_ID, notification)

    }

    @SuppressLint("ResourceAsColor")
    @OptIn(
        ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
        ExperimentalComposeUiApi::class
    )
    private fun buildNotification(): Notification {
        val notificationChannelId = "STORKY_SERVICE_CHANNEL"
        val notificationIntent = Intent(this, MainActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        builder = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle(resources.getString(R.string.foreground_service_title))
            .setContentText(
                (if (showContractionlScreen.value) getString(R.string.contraction) else getString(
                    R.string.length_of_interval
                )) + ": " + convertSecondsToTimeString(currentLengthBetweenContractions.value)
            )
            .setSmallIcon(R.drawable.ic_launcher_monochromatic) // Ensure this is a valid drawable resource
            .setContentIntent(pendingIntent)
            .setOngoing(true) // Ensures the notification cannot be swiped away
            .setSilent(true) // Makes the notification silent
            .setColorized(false)


        if (showContractionlScreen.value) {
            val currentModeType =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val notificationColor = when (currentModeType) {
                Configuration.UI_MODE_NIGHT_YES -> ContextCompat.getColor(
                    this,
                    R.color.service_primary_dark
                )

                Configuration.UI_MODE_NIGHT_NO -> ContextCompat.getColor(
                    this,
                    R.color.service_primary
                )

                else -> ContextCompat.getColor(
                    this,
                    R.color.service_primary
                ) // Default to light theme color
            }
            builder!!.setColorized(true)
                .setColor(notificationColor)
        }
        return builder!!.build()
    }


    private fun startStopwatch() {
        serviceJob = serviceScope.launch {
            while (isRunning) {
                delay(1000) // wait for 1 second
                if (!pauseStopWatch.value) currentLengthBetweenContractions.value += 1
                updateNotification() // Update the notification with the new value

            }
        }
    }


    private fun updateNotification() {
        notification = buildNotification()
        notificationManager?.notify(1, notification)
    }

    private fun stopStopwatch() {
        isRunning = false
        serviceJob?.cancel() // Cancel the running coroutine
        serviceJob = null
    }
}