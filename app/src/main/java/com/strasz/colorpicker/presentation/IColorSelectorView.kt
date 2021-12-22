package com.strasz.colorpicker.presentation

import android.graphics.Bitmap
import android.view.MotionEvent
import io.reactivex.Observable

interface IColorSelectorView {
    val imageTouched: Observable<MotionEvent>
    val currentImageBitmap: Bitmap
}