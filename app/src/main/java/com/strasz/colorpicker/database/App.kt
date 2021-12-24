package com.strasz.colorpicker.database

import android.app.Application
import androidx.room.Room

class App : Application() {
    companion object {
        // Initialize database
        private lateinit var db: ColorDb
        const val colorDbName = "colorDb"
        val colorDb
            get() = db.colorModelDao()
    }

    override fun onCreate() {
        super.onCreate();
        db = synchronized(Unit){
            Room.databaseBuilder(
                    applicationContext,
                    ColorDb::class.java,
                    colorDbName
            ).build()
        }
    }
}