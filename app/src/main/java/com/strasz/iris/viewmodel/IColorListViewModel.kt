package com.strasz.iris.viewmodel

import com.strasz.iris.database.ColorModel
import io.reactivex.Observable

interface IColorListViewModel {
    fun getSavedColorList(): Observable<List<ColorModel>>
    fun removeColor(color: ColorModel)
}