package com.strasz.iris.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.strasz.iris.database.App.Companion.colorTableName
import io.reactivex.Observable

@Dao
interface ColorModelDao {
    @Query("SELECT * FROM $colorTableName")
    fun getAll(): Observable<List<ColorModel>>

    @Query("SELECT * FROM $colorTableName WHERE hex_str LIKE :hex")
    fun findByName(hex: String): ColorModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(color: ColorModel)

    @Delete
    fun delete(color: ColorModel)
}