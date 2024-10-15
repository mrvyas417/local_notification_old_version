package com.example.local_notifcation_scl

import android.content.Intent
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.example.local_notifcation_scl.websocket"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start WebSocket Service when the app launches
      //  startWebSocketService()
    }

    // Method to start WebSocket Service
    private fun startWebSocketService() {
        val intent = Intent(this, WebSocketService::class.java)
        startForegroundService(intent) // Use startForegroundService for API 26 and above
    }

    override fun configureFlutterEngine(flutterEngine: io.flutter.embedding.engine.FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "startWebSocketService" -> {
                    startWebSocketService()
                    result.success("WebSocket Service Started")
                }
                "stopWebSocketService" -> {
                    val intent = Intent(this, WebSocketService::class.java)
                    stopService(intent)
                    result.success("WebSocket Service Stopped")
                }
                else -> result.notImplemented()
            }
        }
    }
}
