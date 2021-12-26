package com.strasz.colorpicker.database

import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.strasz.colorpicker.database.App.Companion.colorTableName

@Entity(tableName = colorTableName)
data class ColorModel(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "hex_str") @ColorInt val hexVal: Int
)