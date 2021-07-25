package com.rsscool.pomodoro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Timer::class], version = 1, exportSchema = false)
abstract class TimerDatabase : RoomDatabase() {
    abstract val timerDatabaseDao: TimerDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: TimerDatabase? = null

        fun getInstance(context: Context): TimerDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TimerDatabase::class.java,
                        "timers_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}