package com.strasz.iris.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.strasz.iris.database.App.Companion.COLOR_TABLE_NAME
import io.reactivex.Observable

/**************************************************************
 ***        Originally written by Oliver Straszynski        ***
 ***        https://github.com/broliver12/                  ***
 ***        Subject to MIT License (c) 2021                 ***
 **************************************************************/

@Dao
interface ColorModelDao {
    @Query("SELECT * FROM $COLOR_TABLE_NAME")
    fun getAll(): Observable<List<ColorModel>>

    @Query("SELECT * FROM $COLOR_TABLE_NAME WHERE hex_str LIKE :hex")
    fun findByName(hex: String): ColorModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(color: ColorModel)

    @Delete
    fun delete(color: ColorModel)
}