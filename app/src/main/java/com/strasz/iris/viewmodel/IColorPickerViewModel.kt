package com.strasz.iris.viewmodel

import android.net.Uri
import androidx.annotation.ColorInt
import com.strasz.iris.view.IColorPickerView
import io.reactivex.Observable

interface IColorPickerViewModel {

    fun bindView(view: IColorPickerView)

    fun reloadLastImage()

    fun updateCurrentImage(newImageUrl: Uri)

    fun xValueText(): Observable<String>

    fun yValueText(): Observable<String>

    fun pixelColorInt(): Observable<Int>

    fun rgbText(): Observable<String>

    fun hexText(): Observable<String>

    fun cmykText(): Observable<String>

    fun confirmSave(@ColorInt color: Int)

    fun saveOnClick(): Observable<Unit>

    val selectedImage: Observable<Uri>
}