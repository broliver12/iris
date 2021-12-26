package com.strasz.colorpicker.viewmodel

import com.strasz.colorpicker.database.ColorModel
import io.reactivex.Observable

interface IColorListViewModel {
    fun getSavedColorList(): Observable<List<ColorModel>>
    fun removeColor(color: ColorModel)
}