package com.nhatdo.androidscreeningtest.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nhatdo.androidscreeningtest.utilities.TimeConverterUtil

class TimerViewModel  : ViewModel(){
     val liveData = MutableLiveData<Long>()

    fun updateTimer(milliseconds: Long){
        liveData.value = milliseconds
    }
    fun  millisUntilFinished() : Int = liveData.value?.toInt() ?: 0
    fun  millisToText(): String = TimeConverterUtil.convertMilliSecondstoRealTime(liveData.value ?: 0)

}