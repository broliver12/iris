package com.strasz.colorpicker.database

import androidx.room.ColumnInfo
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@ProvidedTypeConverter
class EntityConverters {
    // PhotoUrls
    @TypeConverter
    fun toJson(model: ColorModel) = Gson().toJson(model)
    @TypeConverter
    fun toColorModel(json: String) = Gson().fromJson(json, ColorModel::class.java)
}