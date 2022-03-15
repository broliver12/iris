package com.strasz.iris.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**************************************************************
 ***        Originally written by Oliver Straszynski        ***
 ***        https://github.com/broliver12/                  ***
 ***        Subject to MIT License (c) 2021                 ***
 **************************************************************/

@Database(entities = [ColorModel::class], version = 1, exportSchema = true)
abstract class ColorDatabase : RoomDatabase() {
    abstract fun colorModelDao(): ColorModelDao
}