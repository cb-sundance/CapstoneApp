package com.example.capstoneapp

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class NotificationWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val CHANNEL_ID = "capstone_notifications"
        const val NOTIFICATION_ID = 1001
    }

    override suspend fun doWork(): Result {
        try {
            // Build and show notification
            val title = "Capstone App"
            val text = "Thanks for opening the app — keep going with the project!"
            val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(NOTIFICATION_ID, builder.build())
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}
