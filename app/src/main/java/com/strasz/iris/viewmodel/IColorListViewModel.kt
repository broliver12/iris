package com.strasz.iris.viewmodel

import com.strasz.iris.database.ColorModel
import io.reactivex.Observable

/**************************************************************
 ***        Originally written by Oliver Straszynski        ***
 ***        https://github.com/broliver12/                  ***
 ***        Subject to MIT License (c) 2021                 ***
 **************************************************************/

interface IColorListViewModel {
    fun getSavedColorList(): Observable<List<ColorModel>>
    fun removeColor(color: ColorModel)
}