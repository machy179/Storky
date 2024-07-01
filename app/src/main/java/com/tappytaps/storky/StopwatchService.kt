package com.tappytaps.storky

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationCompat
import com.tappytaps.storky.repository.ContractionsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopwatchService : Service() {

    @Inject
    lateinit var repository: ContractionsRepository

    private var serviceJob: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    private var isRunning = false
    private var currentLengthBetweenContractions = 0
    private var pauseStopWatch = false

    private var showContractionlScreen = false

    override fun onCreate() {
        Log.d("StorkyService:","onCreateservice")
        super.onCreate()
        startForegroundService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("StorkyService:","startservice")
        currentLengthBetweenContractions = intent?.getIntExtra("currentLengthBetweenContractions", 0) ?: 0
        Log.d("StorkyService:","currentLengthBetweenContractions="+currentLengthBetweenContractions.toString())
        pauseStopWatch = intent?.getBooleanExtra("pauseStopWatch", false) ?: false
        showContractionlScreen = intent?.getBooleanExtra("showContractionlScreen", false) ?: false
        isRunning = true
        Log.d("StorkyService:","onStartCommand pauseStopWatch="+pauseStopWatch.toString())
  //      if (showContractionlScreen || currentLengthBetweenContractions>0)  { //checking, whether stop watch is running
            startStopwatch()
  //      }

        return START_STICKY
    }

    override fun onDestroy() {
        Log.d("StorkyService:","onDestroyservice")
        sendUpdateToViewModel()
        super.onDestroy()
        stopStopwatch()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
        ExperimentalComposeUiApi::class
    )
    private fun startForegroundService() {
        val notificationChannelId = "STORKY_SERVICE_CHANNEL"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Stopwatch Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification: Notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Stopwatch Running")
            .setContentText("The stopwatch is running in the background")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ensure this is a valid drawable resource
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1109, notification)
    }

    private fun startStopwatch() {
        serviceJob = serviceScope.launch {
            while (isRunning) {
                delay(1000) // wait for 1 second
                if (!pauseStopWatch) currentLengthBetweenContractions += 1
   //             sendUpdateToViewModel()
            }
        }
    }

    private fun stopStopwatch() {
        isRunning = false
        serviceJob?.cancel() // Cancel the running coroutine
     //   sendUpdateToViewModel()
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