package com.strasz.iris.database

import android.app.Application
import androidx.room.Room

class App : Application() {
    companion object {
        private lateinit var database: ColorDatabase
        private const val DB_NAME: String = "colorDatabase"
        const val COLOR_TABLE_NAME: String = "colorTable"
        val colorDao
            get() = database.colorModelDao()
    }

    override fun onCreate() {
        super.onCreate();
        database = synchronized(Unit) {
            Room.databaseBuilder(
                applicationContext,
                ColorDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}