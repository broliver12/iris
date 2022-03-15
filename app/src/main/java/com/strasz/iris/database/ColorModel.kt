package com.strasz.iris.database

import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.strasz.iris.database.App.Companion.COLOR_TABLE_NAME

/**************************************************************
 ***        Originally written by Oliver Straszynski        ***
 ***        https://github.com/broliver12/                  ***
 ***        Subject to MIT License (c) 2021                 ***
 **************************************************************/

@Entity(tableName = COLOR_TABLE_NAME)
data class ColorModel(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "hex_str") @ColorInt val hexVal: Int
)