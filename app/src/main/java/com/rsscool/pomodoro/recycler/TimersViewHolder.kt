package com.rsscool.pomodoro.recycler

import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.rsscool.pomodoro.R
import com.rsscool.pomodoro.database.Timer
import com.rsscool.pomodoro.databinding.TimerItemBinding
import com.rsscool.pomodoro.displayTime
import com.rsscool.pomodoro.viewModel.TimerListener
import kotlin.math.abs

class TimersViewHolder(
    private val binding: TimerItemBinding,
    private val listener: TimerListener,
    private val resources: Resources
) : RecyclerView.ViewHolder(binding.root) {

    private var time: CountDownTimer? = null

    fun bind(timer: Timer) {
        binding.textView.text = timer.currentTimeMilli.displayTime()
        binding.customProgress.setPeriod(timer.periodTimeMilli)
        binding.customProgress.setCurrent(timer.currentTimeMilli)

        if (timer.isStarted) {
            startTimer(timer)
        } else {
            stopTimer(timer)
        }

        initButtonsListeners(timer)
    }

    private fun initButtonsListeners(timer: Timer) {
        binding.startStopButton.setOnClickListener {
            if (timer.isStarted) {
                listener.stop(timer.id, timer.currentTimeMilli)
            } else {
                listener.start(timer.id)
            }
        }

        binding.deleteButton.setOnClickListener { listener.delete(timer.id) }
    }

    private fun startTimer(timer: Timer) {
        val drawable = resources.getDrawable(R.drawable.ic_baseline_pause_24)
        binding.startStopButton.setImageDrawable(drawable)

        time?.cancel()
        time = getCountDownTimer(timer)
        time?.start()

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer(timer: Timer) {
        val drawable = resources.getDrawable(R.drawable.ic_baseline_play_arrow_24)
        binding.startStopButton.setImageDrawable(drawable)
        time?.cancel()
        listener.update(timer)

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private fun getCountDownTimer(timer: Timer): CountDownTimer {
        return object : CountDownTimer(timer.currentTimeMilli, UNIT_TEN_MS) {
            val startTime = System.currentTimeMillis()
            override fun onTick(millisUntilFinished: Long) {
                timer.currentTimeMilli = millisUntilFinished
                binding.textView.text =
                    timer.currentTimeMilli.displayTime()
                binding.customProgress.setCurrent(timer.currentTimeMilli)
            }

            override fun onFinish() {
                timer.currentTimeMilli = timer.periodTimeMilli
                timer.isStarted = false
                binding.textView.text = timer.currentTimeMilli.displayTime()
                stopTimer(timer)
            }
        }
    }

    private companion object {
        private const val UNIT_TEN_MS = 10L
    }
}