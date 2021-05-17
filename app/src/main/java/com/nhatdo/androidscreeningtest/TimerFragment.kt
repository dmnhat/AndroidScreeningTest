package com.nhatdo.androidscreeningtest

import android.Manifest.permission.VIBRATE
import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleObserver
import com.nhatdo.androidscreeningtest.databinding.FragmentTimerBinding
import com.nhatdo.androidscreeningtest.utilities.*
import com.nhatdo.androidscreeningtest.viewmodels.TimerViewModel


class TimerFragment : BaseFragment() , NumberPicker.OnValueChangeListener, LifecycleObserver{

    private lateinit var binding: FragmentTimerBinding
    private lateinit var alertDialog: AlertDialog
    private lateinit var vibrator : Vibrator


    private  val viewModel : TimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requireActivity().requestPermissions(arrayOf(VIBRATE),100)
        }
        binding = FragmentTimerBinding.inflate(inflater, container, false)

        setupBinding()
        observerViewModel()

        return binding.root

    }

    private fun setupBinding(){

       val  builder = AlertDialog.Builder(requireContext())
        builder.setTitle(null)
        builder.setMessage("Timer is up")
        builder.setPositiveButton("OK") { dialog, which ->
            viewModel.showNotification.postValue(false)
            cancelScheduleNotification(requireContext())
            dialog.cancel()
        }
        alertDialog = builder.create()

        binding.numberPickerHours.minValue = HOURS_VALUE_MIN
        binding.numberPickerHours.maxValue = HOURS_VALUE_MAX

        binding.numberPickerMinutes.minValue = MINUTES_VALUE_MIN
        binding.numberPickerMinutes.maxValue = MINUTES_VALUE_MAX

        binding.numberPickerSeconds.minValue = SECONDS_VALUE_MIN
        binding.numberPickerSeconds.maxValue = SECONDS_VALUE_MAX

        binding.numberPickerHours.setOnValueChangedListener(this)
        binding.numberPickerMinutes.setOnValueChangedListener(this)
        binding.numberPickerSeconds.setOnValueChangedListener(this)

        binding.setOnStartStopClick {
            viewModel.startStopTimer()
        }
        binding.setOnCancelClick {
            viewModel.cancelTimer(false)
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

    }

    private fun observerViewModel(){
        viewModel.millisSecondsLeft.observe(viewLifecycleOwner)  {
            binding.progressCircular.progress = it.toInt()
            binding.tvTimer.text = viewModel.millisToText()
        }
        viewModel.millisSeconds.observe(viewLifecycleOwner){

            binding.progressCircular.max = it.toInt()
            binding.progressCircular.progress = 0

        }
        viewModel.status.observe(viewLifecycleOwner){
            binding.btnStartStop.text = viewModel.actionStartStopLabel()
            when(viewModel.status.value!!){
                Status.IDLE -> {
                    binding.tvTimer.visibility = View.INVISIBLE
                    binding.timePicker.visibility = View.VISIBLE
                    binding.progressCircular.max = viewModel.millisSeconds?.value!!.toInt()
                    binding.progressCircular.progress = 0
                }
                Status.START -> {
                    binding.tvTimer.visibility = View.VISIBLE
                    binding.timePicker.visibility = View.INVISIBLE
                }
                Status.PAUSE -> {

                    binding.tvTimer.visibility = View.VISIBLE
                    binding.timePicker.visibility = View.INVISIBLE
                }
            }
        }
        viewModel.showNotification.observe(viewLifecycleOwner){
            if(it){
                showDialog()
            }else{
                hideDialog()
            }

        }
    }


    private fun showDialog(){

        if(isAppInForeground){
            if(!alertDialog.isShowing) {
                alertDialog.show()
            }
        }
    }


    private fun hideDialog(){
        if(alertDialog!!.isShowing){
            alertDialog.cancel()
        }
        vibrator.cancel()
        if(isAppInForeground) {
            cancelNotification(requireContext())
        }
    }

    override fun onValueChange(picker: NumberPicker?, p1: Int, p2: Int) {

        when (picker!!){
            binding.numberPickerHours, binding.numberPickerMinutes, binding.numberPickerSeconds -> {
                val hours = binding.numberPickerHours.value * 3600 * 1000
                val minutes = binding.numberPickerMinutes.value * 60 * 1000
                val seconds = binding.numberPickerSeconds.value * 1000
                viewModel.millisSeconds.postValue((hours + minutes + seconds).toLong())
            }
            else -> return
        }
    }
}