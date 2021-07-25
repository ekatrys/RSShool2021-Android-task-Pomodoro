package com.rsscool.pomodoro.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timers_list_table")

data class Timer(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "is_started")
    var isStarted: Boolean = false,

    @ColumnInfo(name = "period_time_milli")
    var periodTimeMilli: Long = 0L,

    @ColumnInfo(name = "current_time_milli")
    var currentTimeMilli: Long = 0L,
)