package com.example.capstoneapp

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        // Send daily notification
        NotificationUtils.showSimpleNotification(
            applicationContext,
            "Daily Reminder",
            "Don't forget to check your Capstone App today!"
        )

        return Result.success()
    }
}


