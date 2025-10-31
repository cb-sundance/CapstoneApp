package com.example.capstoneapp

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class SyncWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // This worker posts a simple notification to remind user to check the app.
        NotificationUtils.showSimpleNotification(
            context = applicationContext,
            title = "Capstone App",
            message = "Don't forget to check your app updates!"
        )
        return Result.success()
    }
}
