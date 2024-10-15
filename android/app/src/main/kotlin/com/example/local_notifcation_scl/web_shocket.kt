package com.example.local_notifcation_scl

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.Nullable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WebSocketService : Service() {
    private val CHANNEL_ID = "WebSocketServiceChannel"
    private lateinit var webSocket: WebSocket
    private val client = OkHttpClient()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        // Build the notification for the foreground service
        val notification = buildForegroundNotification()
        startForeground(1, notification) // Start the service in the foreground

        // Start WebSocket connection
        startWebSocketConnection()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "WebSocket Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket.close(1000, "Service stopped")
    }

    private fun startWebSocketConnection() {
        // Ensure WebSocket connection is established
        val request = Request.Builder().url("ws://10.0.2.2:8080").build()
        webSocket = client.newWebSocket(request, EchoWebSocketListener())
    }

    private inner class EchoWebSocketListener : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            // Handle WebSocket message
            showNotification(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            onMessage(webSocket, bytes.toString())
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            // Handle WebSocket failure
            //showNotification("WebSocket error: ${t.message}")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            // Handle WebSocket closure 


          //  showNotification("WebSocket closed: $reason")
        }

        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            // Notify that the WebSocket connection is open
          //  showNotification("WebSocket connected")
        }
    }

    private fun buildForegroundNotification(): Notification {
        // Create an explicit intent for the activity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
       
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Checking for the notification ")
            .setContentText("")
            .setSmallIcon(R.drawable.ic_notification) // Your notification icon
            .setContentIntent(pendingIntent) // Set the intent for notification tap
           // .setOngoing(true) // Makes the notification ongoing (non-dismissable)
            .build()
    }

    private fun showNotification(message: String) {
        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("New WebSocket Message")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification) // Your notification icon
            .setAutoCancel(true) // Automatically removes the notification when tapped
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(2, notification)
    }
}
