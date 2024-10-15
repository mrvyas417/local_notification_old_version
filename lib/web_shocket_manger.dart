import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class WebSocketManager {
  static const platform =
      MethodChannel('com.example.local_notifcation_scl.websocket');

  // Start the WebSocket service
  static Future<void> startWebSocketService() async {
    try {
      await platform.invokeMethod('startWebSocketService');
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print("Failed to start WebSocket service: ${e.message}");
      }
    }
  }

  // Stop the WebSocket service
  static Future<void> stopWebSocketService() async {
    try {
      await platform.invokeMethod('stopWebSocketService');
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print("Failed to stop WebSocket service: ${e.message}");
      }
    }
  }
}
