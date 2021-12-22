package com.strasz.colorpicker.presentation

import android.view.MotionEvent
import com.strasz.colorpicker.util.ColorUtil
import io.reactivex.Observable

class ColorPickerViewModel {
    private lateinit var xValueObservable: Observable<Int>
    private lateinit var yValueObservable: Observable<Int>
    private lateinit var pixelObservable: Observable<Int>
    private var view: IColorSelectorView? = null

    fun bindView(view: IColorSelectorView) {
        this.view = view
        val imageTouched = view.imageTouched.share()
        val downEvents = imageTouched.filter { event: MotionEvent -> event.action == MotionEvent.ACTION_DOWN }
        xValueObservable = downEvents.map { event: MotionEvent -> event.x.toInt() }
        yValueObservable = downEvents.map { event: MotionEvent -> event.y.toInt() }
        pixelObservable = downEvents.map { event: MotionEvent -> view.currentImageBitmap.getPixel(event.x.toInt(), event.y.toInt()) }
    }

    fun xValueText(): Observable<String> {
        return xValueObservable.map { x: Int -> x.toString() }
    }

    fun yValueText(): Observable<String> {
        return yValueObservable.map { x: Int -> x.toString() }
    }

    fun pixelColorInt(): Observable<Int> {
        return pixelObservable
    }

    fun rgbText(): Observable<String> {
        return pixelObservable.map { colorInput: Int -> ColorUtil.generateRGBString(colorInput) }
    }

    fun hexText(): Observable<String> {
        return pixelObservable.map { colorInput: Int -> ColorUtil.generateHexString(colorInput) }
    }

    fun cmykText(): Observable<String> {
        return pixelObservable.map { colorInput: Int -> ColorUtil.generateCMYKString(colorInput) }
    }
}