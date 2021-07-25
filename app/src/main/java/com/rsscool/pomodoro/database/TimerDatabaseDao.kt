package com.rsscool.pomodoro.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TimerDatabaseDao {

    @Insert
    suspend fun insert(timer: Timer)

    @Update
    suspend fun update(timer: Timer)

    @Query("SELECT * from timers_list_table WHERE id = :key")
    suspend fun get(key: Long): Timer?

    @Query("DELETE FROM timers_list_table")
    suspend fun clear()

    @Query("SELECT * FROM timers_list_table ORDER BY id DESC")
    fun getAllTimers(): LiveData<List<Timer>>

    @Query("SELECT * FROM timers_list_table ORDER BY id DESC LIMIT 1")
    suspend fun getLastTimer(): Timer?

    @Query("DELETE from timers_list_table WHERE id = :key")
    suspend fun deleteTimerWithId(key: Long)

    @Query("SELECT * from timers_list_table WHERE is_started = 'true' ORDER BY id DESC LIMIT 1")
    suspend fun getLastStartedTimer(): Timer?
}