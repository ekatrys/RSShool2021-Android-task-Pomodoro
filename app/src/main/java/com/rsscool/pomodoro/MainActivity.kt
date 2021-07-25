package com.rsscool.pomodoro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProvider
import com.rsscool.pomodoro.database.TimerDatabase
import com.rsscool.pomodoro.databinding.ActivityMainBinding
import com.rsscool.pomodoro.recycler.TimerAdapter
import com.rsscool.pomodoro.viewModel.MainViewModel
import com.rsscool.pomodoro.viewModel.MainViewModelFactory

class MainActivity : AppCompatActivity(), LifecycleObserver {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private var startTime = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataSource = TimerDatabase.getInstance(application).timerDatabaseDao
        val viewModelFactory = MainViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        val adapter = TimerAdapter(viewModel)
        startTime = System.currentTimeMillis()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.timerListRecycler.adapter = adapter

        viewModel.timers.observe(this, {
            it?.let {
                adapter.submitList(it)
            }
        })
        binding.editTextNumberDecimal.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                viewModel.timeInterval.value = java.lang.Long.parseLong(it.toString()) * 1000L * 60L
            } else viewModel.timeInterval.value = null

        }

        binding.addButton.setOnClickListener {
            viewModel.addTimer()
            binding.editTextNumberDecimal.text.clear()
            hideKeyboard(this)
        }
    }

    override fun onStop() {
        super.onStop()
        val k = viewModel.timeToEndLastStartedTimer()
        val p = viewModel.timeToEndLastStartedTimer()
        viewModel.timeToEndLastStartedTimer()
        val lastStartedTimer = viewModel.getLastStartedTimer

        val startIntent = Intent(this, ForegroundService::class.java)
        startIntent.putExtra(COMMAND_ID, COMMAND_START)
        startIntent.putExtra(TIMER_LAST_VALUE_MS, lastStartedTimer?.currentTimeMilli)
        startIntent.putExtra(LAST_SYSTEM_TIME, System.currentTimeMillis())
        startService(startIntent)
    }

    override fun onStart() {
        super.onStart()
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
    }

}