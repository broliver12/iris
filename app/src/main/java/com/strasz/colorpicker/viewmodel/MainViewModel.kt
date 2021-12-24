package com.strasz.colorpicker.viewmodel

import android.view.MotionEvent
import androidx.annotation.ColorInt
import com.strasz.colorpicker.database.ColorModel
import com.strasz.colorpicker.database.ColorModelDao
import com.strasz.colorpicker.view.IColorPickerView
import com.strasz.colorpicker.util.ColorUtil
import io.reactivex.Observable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(
        private val database: ColorModelDao
) : IColorPickerViewModel, IColorListViewModel {
    private lateinit var xValueObservable: Observable<Int>
    private lateinit var yValueObservable: Observable<Int>
    private lateinit var pixelObservable: Observable<Int>
    private var view: IColorPickerView? = null

    fun bindView(view: IColorPickerView) {
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

    fun confirmSave(@ColorInt color: Int) {
        GlobalScope.launch{
            database.insert(ColorModel(color, color))
        }
    }

    fun getList() : List<ColorModel> {
        val list = arrayListOf<ColorModel>()
        runBlocking {
            GlobalScope.launch{
                list.addAll(database.getAll())
            }
        }
        return list
    }
}