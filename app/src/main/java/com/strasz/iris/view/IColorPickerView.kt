package com.strasz.iris.view

import android.graphics.Bitmap
import android.view.MotionEvent
import io.reactivex.Observable

/**************************************************************
 ***        Originally written by Oliver Straszynski        ***
 ***        https://github.com/broliver12/                  ***
 ***        Subject to MIT License (c) 2021                 ***
 **************************************************************/

interface IColorPickerView {
    val imageTouched: Observable<MotionEvent>
    val currentImageBitmap: Bitmap
}