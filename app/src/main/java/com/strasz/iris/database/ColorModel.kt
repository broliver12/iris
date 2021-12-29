package com.strasz.iris.database

import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.strasz.iris.database.App.Companion.COLOR_TABLE_NAME

@Entity(tableName = COLOR_TABLE_NAME)
data class ColorModel(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "hex_str") @ColorInt val hexVal: Int
)