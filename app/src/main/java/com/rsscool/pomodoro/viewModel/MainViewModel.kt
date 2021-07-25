package com.rsscool.pomodoro.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsscool.pomodoro.database.Timer
import com.rsscool.pomodoro.database.TimerDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface TimerListener {
    fun start(id: Long)
    fun stop(id: Long, currentMs: Long)
    fun delete(id: Long)
    fun update(timer: Timer)
}

class MainViewModel(val database: TimerDatabaseDao) : ViewModel(),
    TimerListener {
    val timers = database.getAllTimers()
    var timeInterval = MutableLiveData<Long?>()
    var getLastStartedTimer: Timer? = null

    fun addTimer() {
        viewModelScope.launch {
            if (timeInterval.value != null) {
                val newNTimer = Timer()
                newNTimer.periodTimeMilli = timeInterval.value!!
                newNTimer.currentTimeMilli = newNTimer.periodTimeMilli
                newNTimer.id = getLastId()?.plus(1) ?: 0L
                insert(newNTimer)
            }
        }
    }

    fun timeToEndLastStartedTimer() =
        viewModelScope.launch {
            getLastStartedTimer = getLastStartedTimer()
        }

    override fun start(id: Long) {
        viewModelScope.launch {
            updateStart(id)
        }
    }

    override fun stop(id: Long, currentMs: Long) {
        viewModelScope.launch {
            updateStop(id, currentMs)
        }
    }

    override fun delete(id: Long) {
        viewModelScope.launch {
            deleteFromDB(id)
        }
    }

    override fun update(timer: Timer) {
        viewModelScope.launch {
            updateTimer(timer)
        }
    }


    private suspend fun insert(timer: Timer) {
        withContext(Dispatchers.IO) {
            database.insert(timer)
        }
    }

    private suspend fun deleteFromDB(id: Long) {
        withContext(Dispatchers.IO) {
            database.deleteTimerWithId(id)
        }
    }

    private suspend fun getLastId(): Long? =
        withContext(Dispatchers.IO) {
            database.getLastTimer()?.id
        }

    private suspend fun updateStart(id: Long) =
        withContext(Dispatchers.IO) {
            val timer = database.get(id)!!
            timer?.isStarted = true
            database.update(timer!!)
        }

    private suspend fun updateStop(id: Long, currentMs: Long) =
        withContext(Dispatchers.IO) {
            val timer = database.get(id)!!
            timer.isStarted = false
            timer.currentTimeMilli = currentMs
            database.update(timer)
        }

    private suspend fun updateTimer(timer: Timer) =
        withContext(Dispatchers.IO) {
            database.update(timer)
        }

    private suspend fun getLastStartedTimer(): Timer? =
        withContext(Dispatchers.IO) {
            database.getLastStartedTimer()
        }
}
