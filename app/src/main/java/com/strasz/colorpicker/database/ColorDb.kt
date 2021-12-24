package com.strasz.colorpicker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ColorModel::class], version = 1, exportSchema = true)
@TypeConverters(EntityConverters::class)
abstract class ColorDb : RoomDatabase(){
    abstract fun colorModelDao() : ColorModelDao
}