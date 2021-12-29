package com.strasz.iris.viewmodel

import android.net.Uri
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strasz.iris.database.ColorModel
import com.strasz.iris.database.ColorModelDao
import com.strasz.iris.util.ColorUtil
import com.strasz.iris.view.IColorPickerView
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val database: ColorModelDao
) : ViewModel(), IColorPickerViewModel, IColorListViewModel {
    private lateinit var xValueObservable: Observable<Int>
    private lateinit var yValueObservable: Observable<Int>
    private lateinit var pixelObservable: Observable<Int>
    private lateinit var colorPickerView: IColorPickerView
    private val changedImage = PublishSubject.create<Uri>()
    private var lastImage: Uri? = null
    override val selectedImage: Observable<Uri> = changedImage

    override fun bindView(view: IColorPickerView) {
        colorPickerView = view
        val downEvents = colorPickerView.imageTouched.share()
            .filter { e: MotionEvent -> e.action == MotionEvent.ACTION_DOWN }
        xValueObservable = downEvents.map { e: MotionEvent -> e.x.toInt() }
        yValueObservable = downEvents.map { e: MotionEvent -> e.y.toInt() }
        pixelObservable = downEvents.map { e: MotionEvent ->
            view.currentImageBitmap.getPixel(
                e.x.toInt(),
                e.y.toInt()
            )
        }
    }

    override fun reloadLastImage() {
        changedImage.onNext(lastImage ?: "".toUri())
    }

    override fun updateCurrentImage(newImageUrl: Uri) {
        lastImage = newImageUrl
        changedImage.onNext(newImageUrl)
    }

    override fun xValueText(): Observable<String> {
        return xValueObservable.map { x: Int -> x.toString() }
    }

    override fun yValueText(): Observable<String> {
        return yValueObservable.map { x: Int -> x.toString() }
    }

    override fun pixelColorInt(): Observable<Int> {
        return pixelObservable
    }

    override fun rgbText(): Observable<String> {
        return pixelObservable.map { colorInput: Int -> ColorUtil.formatRgb(colorInput) }
    }

    override fun hexText(): Observable<String> {
        return pixelObservable.map { colorInput: Int -> ColorUtil.formatHex(colorInput) }
    }

    override fun cmykText(): Observable<String> {
        return pixelObservable.map { colorInput: Int -> ColorUtil.formatCmyk(colorInput) }
    }

    override fun confirmSave(@ColorInt color: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            database.insert(ColorModel(color, color))
        }
    }

    override fun saveOnClick(): Observable<Unit> =
        Observable.just(Unit).delay(SAVE_DELAY_MILLIS, TimeUnit.MILLISECONDS)

    override fun getSavedColorList(): Observable<List<ColorModel>> {
        return Observable.just(Unit).observeOn(Schedulers.io()).flatMap { database.getAll() }
    }

    override fun removeColor(color: ColorModel) {
        viewModelScope.launch(Dispatchers.IO) {
            database.delete(color)
        }
    }

    companion object {
        private const val SAVE_DELAY_MILLIS = 2200L
    }
}