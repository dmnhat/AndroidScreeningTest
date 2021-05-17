package com.nhatdo.androidscreeningtest.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nhatdo.androidscreeningtest.utilities.showTimeUpNotification

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotifyWork(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val myContext  = context
    override suspend fun doWork(): Result {
        withContext(Dispatchers.Main) {
            showTimeUpNotification(myContext)
        }
        return Result.success()
    }
}