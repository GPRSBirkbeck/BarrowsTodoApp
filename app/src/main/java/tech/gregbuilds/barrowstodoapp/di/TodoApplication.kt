package tech.gregbuilds.barrowstodoapp.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import tech.gregbuilds.barrowstodoapp.common.Constants.TODO_CHANNEL_ID

@HiltAndroidApp
class TodoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                TODO_CHANNEL_ID,
                "Todos",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Used for the notification of todos that are due today."

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}