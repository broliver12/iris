package com.strasz.iris.database

import android.app.Application
import androidx.room.Room
import com.strasz.iris.R

class App : Application() {
    companion object {
        private lateinit var db: ColorDatabase
        lateinit var colorDbName: String
            internal set
        const val colorTableName: String = "colorTable"
        val colorDao
            get() = db.colorModelDao()
    }

    override fun onCreate() {
        super.onCreate();
        colorDbName = resources.getString(R.string.database_name)

        db = synchronized(Unit) {
            Room.databaseBuilder(
                    applicationContext,
                    ColorDatabase::class.java,
                    colorDbName
            ).build()
        }
    }
}