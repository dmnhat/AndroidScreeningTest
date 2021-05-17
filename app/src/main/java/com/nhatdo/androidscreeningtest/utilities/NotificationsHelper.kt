package com.nhatdo.androidscreeningtest.utilities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.media.RingtoneManager
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.UriCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation
import androidx.work.WorkManager
import com.google.android.material.internal.ContextUtils.getActivity
import com.nhatdo.androidscreeningtest.MainActivity
import com.nhatdo.androidscreeningtest.R
import com.nhatdo.androidscreeningtest.workers.NotifyWork
import java.util.concurrent.TimeUnit


        fun getChannelNotification(): NotificationChannel? {
            if (SDK_INT >= O) {
                val importance = IMPORTANCE_HIGH

                val channel =
                    NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)

                val ringtoneManager = RingtoneManager.getDefaultUri(TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(CONTENT_TYPE_SONIFICATION).build()
                channel.setSound(ringtoneManager, audioAttributes)
                channel.description = "Timer is up"
                channel.enableVibration(true)
                return channel
            }
            return null

        }
        fun showTimeUpNotification(context: Context) {
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            val notification = NotificationCompat.Builder(context!!.applicationContext, CHANNEL_ID)
                .setContentTitle(context!!.getString(R.string.app_name))
                .setContentText("Timer is up")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(pendingIntent,true)
                .setContentIntent(pendingIntent).setAutoCancel(false).build()

            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            if (SDK_INT >= O) {
                val channel = getChannelNotification()
                notificationManager.createNotificationChannel(channel!!)

            } else {
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
        }

        fun cancelNotification(context: Context) {
            NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
        }

        fun scheduleNotification(delay: Long,context: Context) {
            val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS).build()
            val instanceWorkManager = WorkManager.getInstance(context)

            instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, ExistingWorkPolicy.APPEND_OR_REPLACE, notificationWork).enqueue()
        }
        fun cancelScheduleNotification(context: Context){
            val instanceWorkManager = WorkManager.getInstance(context)
            instanceWorkManager.cancelUniqueWork(NOTIFICATION_WORK).result.apply {
            }
        }



