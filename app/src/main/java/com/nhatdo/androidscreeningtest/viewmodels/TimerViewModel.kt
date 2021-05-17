package com.nhatdo.androidscreeningtest.viewmodels

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nhatdo.androidscreeningtest.utilities.Status
import com.nhatdo.androidscreeningtest.utilities.TimeConverterUtil
import com.nhatdo.androidscreeningtest.utilities.cancelScheduleNotification
import com.nhatdo.androidscreeningtest.utilities.scheduleNotification

class TimerViewModel(application: Application)  : AndroidViewModel(application){

    val millisSeconds = MutableLiveData<Long>()

    val millisSecondsLeft = MutableLiveData<Long>()
    val status = MutableLiveData<Status>()
    val showNotification = MutableLiveData<Boolean>()

    private lateinit var countDownTimer: CountDownTimer

    init {
        status.value = Status.IDLE
        millisSeconds.value = 0
        millisSecondsLeft.value = 0
        showNotification.value = false
    }


    fun  millisUntilFinished() : Int = millisSecondsLeft.value?.toInt() ?: 0
    fun  millisToText(): String = TimeConverterUtil.convertMilliSecondstoRealTime(millisSecondsLeft.value ?: 0)

    fun actionStartStopLabel() : String   {
        return status.value!!.msg
    }

    fun startStopTimer() {
        if (millisSeconds.value!!<=0) {
            return
        }
        when(status.value!!){
            Status.IDLE ->  {

                status.postValue(Status.START)
                millisSecondsLeft.value = millisSeconds.value
                scheduleNotification(millisSecondsLeft.value!!,getApplication())
                countDownTimer = object : CountDownTimer(millisSecondsLeft.value!!, 10) {

                    override fun onTick(millisUntilFinished: Long) {
                        millisSecondsLeft.postValue(millisUntilFinished)
                    }

                    override fun onFinish() {
                        cancelTimer(true)
                    }
                }
                countDownTimer.start()
            }
            Status.START -> {
                cancelScheduleNotification(getApplication())
                status.postValue(Status.PAUSE)
                countDownTimer.cancel()

            }
            Status.PAUSE -> {
                status.postValue(Status.START)
                scheduleNotification(millisSecondsLeft.value!!,getApplication())
                countDownTimer = object : CountDownTimer(millisSecondsLeft.value!!, 10) {

                    override fun onTick(millisUntilFinished: Long) {
                        millisSecondsLeft.postValue(millisUntilFinished)
                    }

                    override fun onFinish() {
                        cancelTimer(true)


                    }
                }
                countDownTimer.start()
            }
            else -> return
        }

    }

    fun cancelTimer(showNotif: Boolean){
        showNotification.postValue(showNotif)
        status.postValue(Status.IDLE)
        millisSecondsLeft.postValue(0)
        if (status.value==Status.START){
            countDownTimer.cancel()
        }
    }

    fun isCancelEnabled(): Boolean = status.value == Status.START
    fun isStartStopEnabled() : Boolean = millisSeconds.value!! > 0



}