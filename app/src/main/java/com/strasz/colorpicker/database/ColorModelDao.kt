package com.strasz.colorpicker.database

import androidx.room.*

@Dao
interface ColorModelDao {
    @Query("SELECT * FROM colors")
    fun getAll(): List<ColorModel>

    @Query("SELECT * FROM colors WHERE hex_str LIKE :hex")
    fun findByName(hex: String): ColorModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(color: ColorModel)

    @Delete
    fun delete(color: ColorModel)
}