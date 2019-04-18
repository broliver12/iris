package com.strasz.color_value_calculator.viewmodel;

import android.graphics.Color;
import android.view.MotionEvent;

import com.strasz.color_value_calculator.Utils.ColorUtils;
import com.strasz.color_value_calculator.view.IColorSelectorView;

import androidx.core.util.Pair;
import io.reactivex.Observable;

public class ColorSelectorViewModel {

    private Observable<Integer> xValueObservable;
    private Observable<Integer> yValueObservable;
    private Observable<Integer> pixelObservable;
    private IColorSelectorView view;

    public void init(IColorSelectorView view) {
        this.view = view;

        Observable<MotionEvent> imageTouched = view.getImageTouched();

        Observable<MotionEvent> downEvents = imageTouched.filter(event -> event.getAction() == MotionEvent.ACTION_DOWN);

        Observable<MotionEvent> imageClicked = downEvents;

        xValueObservable = imageClicked.map(event -> (int) event.getX());

        yValueObservable = imageClicked.map(event -> (int) event.getY());

        pixelObservable = imageClicked.map(event -> view.getCurrentImageBitmap().getPixel((int) event.getX(), (int) event.getY()));
    }

    public Observable<String> xValueText() {
        return xValueObservable.map(x -> x.toString());
    }

    public Observable<String> yValueText() {
        return yValueObservable.map(x -> x.toString());
    }

    public Observable<Integer> pixelColorInt() {
        return pixelObservable;
    }

    public Observable<String> rgbText() {
        return pixelObservable.map(ColorUtils::generateRGBString);
    }

    public Observable<String> hexText() {
        return pixelObservable.map(ColorUtils::generateHexString);
    }

    public Observable<String> cmykText() {
        return pixelObservable.map(ColorUtils::generateCMYKString);
    }


}
