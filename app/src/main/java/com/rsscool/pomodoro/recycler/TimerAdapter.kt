package com.rsscool.pomodoro.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.rsscool.pomodoro.database.Timer
import com.rsscool.pomodoro.databinding.TimerItemBinding
import com.rsscool.pomodoro.viewModel.TimerListener

class TimerAdapter(private val listener: TimerListener) :
    ListAdapter<Timer, TimersViewHolder>(TimerDiffCallback()) {

    override fun onBindViewHolder(holder: TimersViewHolder, position: Int) =
        holder.bind(getItem(position))


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TimersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TimerItemBinding.inflate(layoutInflater, parent, false)
        return TimersViewHolder(binding, listener, binding.root.context.resources)
    }
}

class TimerDiffCallback : DiffUtil.ItemCallback<Timer>() {
    override fun areItemsTheSame(oldItem: Timer, newItem: Timer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Timer, newItem: Timer): Boolean {
        return oldItem == newItem
    }
}