package com.nhatdo.androidscreeningtest

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nhatdo.androidscreeningtest.databinding.FragmentTimerBinding
import com.nhatdo.androidscreeningtest.viewmodels.TimerViewModel

class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding
    private lateinit var countDownTimer: CountDownTimer

    private  val viewModel : TimerViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTimerBinding.inflate(inflater, container, false)
        binding.progressCircular.max = 20000
        binding.progressCircular.progress = 0
        binding.viewModel = viewModel
        viewModel.liveData.observe(viewLifecycleOwner)  {
            binding.progressCircular.progress = viewModel.millisUntilFinished()
            binding.tvTimer.text = viewModel.millisToText()
        }

        startTimer()

        return binding.root

    }

    fun startTimer(){

        countDownTimer = object : CountDownTimer(20000, 10) {
            override fun onTick(millisUntilFinished: Long) {
                viewModel.updateTimer(millisUntilFinished)
            }

            override fun onFinish() {
                viewModel.updateTimer(0)

            }
        }
        countDownTimer.start()
    }
}