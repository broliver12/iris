package com.strasz.colorpicker.view

import android.graphics.Bitmap
import android.view.MotionEvent
import io.reactivex.Observable

interface IColorPickerView {
    val imageTouched: Observable<MotionEvent>
    val currentImageBitmap: Bitmap
}