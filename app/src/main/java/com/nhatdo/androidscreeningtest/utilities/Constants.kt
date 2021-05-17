package com.nhatdo.androidscreeningtest.utilities

import android.os.Build
import com.nhatdo.androidscreeningtest.BuildConfig
import com.nhatdo.androidscreeningtest.R

/**
 * Constants used throughout the app.
 */
const val HOURS_VALUE_MIN = 0
const val HOURS_VALUE_MAX = 23

const val MINUTES_VALUE_MIN = 0
const val MINUTES_VALUE_MAX = 59

const val SECONDS_VALUE_MIN = 0
const val SECONDS_VALUE_MAX = 59

const val CHANNEL_ID : String = BuildConfig.APPLICATION_ID
const val CHANNEL_NAME : String = "ALARM APP"

const val NOTIFICATION_ID : Int = 123098
const val LABEL_NOTIFICATION_ID = "NOTIFICATION_ID"
const val NOTIFICATION_WORK = BuildConfig.APPLICATION_ID+"_work"



enum class Status(val msg: String, ) {
    START("Pause"),
    IDLE("Start"),
    PAUSE("Resume")
}