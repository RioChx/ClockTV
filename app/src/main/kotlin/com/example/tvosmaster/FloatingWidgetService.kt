package com.example.tvosmaster

import android.app.*
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.app.NotificationCompat

class FloatingWidgetService : Service() {
    private var windowManager: WindowManager? = null
    private var floatingWidget: View? = null

    companion object {
        const CHANNEL_ID = "TV_MASTER_WIDGET_CHANNEL"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("TV Master Clock")
            .setContentText("Widget Overlay Active")
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setOngoing(true)
            .build()

        // Android 14+ Special Use Foreground Service
        startForeground(1337, notification)

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        floatingWidget = LayoutInflater.from(this).inflate(R.layout.widget_layout, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 100
        }

        windowManager?.addView(floatingWidget, params)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, "Floating Clock Service", NotificationManager.IMPORTANCE_LOW)
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        floatingWidget?.let { windowManager?.removeView(it) }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}