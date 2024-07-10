package com.tappytaps.storky

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.tappytaps.storky.repository.ContractionsRepository
import com.tappytaps.storky.utils.convertSecondsToTimeString
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
    lateinit var repository: ContractionsRepository

    private var serviceJob: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.Default) //bylo IO

    private var isRunning = false
    private var currentLengthBetweenContractions = 0
    private var pauseStopWatch = false

    private var showContractionlScreen = false

    override fun onCreate() {
      //  Log.d("StorkyService:", "onCreateservice")
        super.onCreate()
        startForegroundService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
   //     Log.d("StorkyService:", "startservice")
        currentLengthBetweenContractions =
            intent?.getIntExtra("currentLengthBetweenContractions", 0) ?: 0
/*        Log.d(
            "StorkyService:",
            "currentLengthBetweenContractions=" + currentLengthBetweenContractions.toString()
        )*/
        pauseStopWatch = intent?.getBooleanExtra("pauseStopWatch", false) ?: false
        showContractionlScreen = intent?.getBooleanExtra("showContractionlScreen", false) ?: false
        isRunning = true
//        Log.d("StorkyService:", "onStartCommand pauseStopWatch=" + pauseStopWatch.toString())
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
    //    Log.d("StorkyService:", "onDestroyservice")
        sendUpdateToViewModel()
        super.onDestroy()
        stopStopwatch()

        // we need this release because of Doze Mode
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
     //       stopForeground(true)
       //     stopSelf()
        } catch (e: Exception) {
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @OptIn(
        ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
        ExperimentalComposeUiApi::class
    )
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
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notification = buildNotification()
        startForeground(1, notification)

    }

    @SuppressLint("ResourceAsColor")
    @OptIn(
        ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
        ExperimentalComposeUiApi::class
    )
    private fun buildNotification(): Notification {
        val notificationChannelId = "STORKY_SERVICE_CHANNEL"
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle(resources.getString(R.string.foreground_service_title))
            .setContentText(
                (if (showContractionlScreen) getString(R.string.contraction) else getString(
                    R.string.length_of_interval
                )) + ": " + convertSecondsToTimeString(currentLengthBetweenContractions)
            )
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ensure this is a valid drawable resource
            .setContentIntent(pendingIntent)
            .setOngoing(true) // Ensures the notification cannot be swiped away
            .setSilent(true) // Makes the notification silent


        if (showContractionlScreen) {
       //     val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            val currentModeType = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val notificationColor = when (currentModeType) {
                Configuration.UI_MODE_NIGHT_YES -> ContextCompat.getColor(this, R.color.service_primary_dark)
                Configuration.UI_MODE_NIGHT_NO -> ContextCompat.getColor(this, R.color.service_primary)
                else -> ContextCompat.getColor(this, R.color.service_primary) // Default to light theme color
            }
            builder.setColorized(true)
                .setColor(notificationColor)
        }

        return builder.build()
    }


    private fun startStopwatch() {
        serviceJob = serviceScope.launch {
            while (isRunning) {
                delay(1000) // wait for 1 second
                if (!pauseStopWatch) currentLengthBetweenContractions += 1
                updateNotification() // Update the notification with the new value
            }
        }
    }


    private fun updateNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = buildNotification()
        notificationManager.notify(1, notification)
    }

    private fun stopStopwatch() {
        isRunning = false
        serviceJob?.cancel() // Cancel the running coroutine
    }

    private fun sendUpdateToViewModel() {
        val intent = Intent("STOPWATCH_UPDATE").apply {
            putExtra("currentLengthBetweenContractions", currentLengthBetweenContractions)
            putExtra("pauseStopWatch", pauseStopWatch)
            putExtra("showContractionlScreen", showContractionlScreen)
        }
        sendBroadcast(intent)
    }
}