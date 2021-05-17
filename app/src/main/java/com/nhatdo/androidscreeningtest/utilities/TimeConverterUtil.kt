package com.nhatdo.androidscreeningtest.utilities

import androidx.core.util.TimeUtils
import java.util.concurrent.TimeUnit

class TimeConverterUtil {

    companion object {
        fun  convertMilliSecondstoRealTime(milliseconds: Long) =
            String.format(
                "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
            )
    }

}