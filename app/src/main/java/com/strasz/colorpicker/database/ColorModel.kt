package com.strasz.colorpicker.database

import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colors")
data class ColorModel(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "hex_str") @ColorInt val hexVal: Int
)