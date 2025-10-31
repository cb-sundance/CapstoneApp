package com.example.capstoneapp

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        // Example background task: send a daily notification
        NotificationUtils.showSimpleNotification(
            applicationContext,
            "Daily Reminder",
            "Don't forget to check your Capstone App today!"
        )

        // Return success
        return Result.success()
    }
}

