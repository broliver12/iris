package com.strasz.iris.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ColorModel::class], version = 1, exportSchema = true)
abstract class ColorDatabase : RoomDatabase() {
    abstract fun colorModelDao(): ColorModelDao
}