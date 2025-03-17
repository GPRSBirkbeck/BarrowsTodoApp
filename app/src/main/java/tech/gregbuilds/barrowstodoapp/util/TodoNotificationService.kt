package tech.gregbuilds.barrowstodoapp.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.BigTextStyle
import tech.gregbuilds.barrowstodoapp.MainActivity
import tech.gregbuilds.barrowstodoapp.R
import tech.gregbuilds.barrowstodoapp.common.Constants.TODO_CHANNEL_ID
import tech.gregbuilds.barrowstodoapp.model.TodoItem
import javax.inject.Inject

interface TodoNotificationService {
    fun showNotification(todoItem: TodoItem)
}

class TodoNotificationServiceImpl @Inject constructor(
    private val context: Context
): TodoNotificationService {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // In the future I would support notifications that tell you how many todos are due today
    override fun showNotification(todoItem: TodoItem) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(context, TODO_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_checklist_24)
            .setContentTitle("Todo due today: ${todoItem.title}")
            .setContentText("Description: ${todoItem.body}")
            .setStyle(BigTextStyle().bigText("Due today: ${todoItem.title}"))
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(1, notification)

    }

}