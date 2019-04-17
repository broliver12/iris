package com.strasz.color_value_calculator.viewmodel;

import android.graphics.Color;
import android.view.MotionEvent;

import com.strasz.color_value_calculator.view.IColorSelectorView;

import io.reactivex.Observable;

public class ColorSelectorViewModel {

    private Observable<Integer> xValueObservable;
    private Observable<Integer> yValueObservable;
    private Observable<Integer> pixelObservable;
    private IColorSelectorView view;

    public void init(IColorSelectorView view) {
        this.view = view;

        Observable<MotionEvent> imageClicked = view.getImageClicked().share();

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
        return pixelObservable.map(x -> {
            int redValue = Color.red(x);
            int blueValue = Color.blue(x);
            int greenValue = Color.green(x);

            return String.format("RGB: (%d, %d, %d)", redValue, greenValue, blueValue);
        });

    }

    public Observable<String> hexText() {
        return pixelObservable.map(x -> {
            int redValue = Color.red(x);
            int blueValue = Color.blue(x);
            int greenValue = Color.green(x);

            return String.format("HEX: #%02X%02X%02X", redValue, greenValue, blueValue);
        });
    }

    public Observable<String> cmykText() {
        return pixelObservable.map(x -> {
            float redValue = Color.red(x);
            float blueValue = Color.blue(x);
            float greenValue = Color.green(x);

            float cyanValue = 1 - (redValue / 255);
            float magentaValue = 1 - (greenValue / 255);
            float yellowValue = 1 - (blueValue / 255);

            float kValue = Math.min(Math.min(cyanValue, magentaValue), yellowValue);

            if (kValue != 1) {
                float sValue = 1 - kValue;

                cyanValue = (cyanValue - kValue) / sValue;

                magentaValue = (magentaValue - kValue) / sValue;

                yellowValue = (yellowValue - kValue) / sValue;
            }

            int cValue = Math.round(cyanValue * 100);
            int mValue = Math.round(magentaValue * 100);
            int yValue = Math.round(yellowValue * 100);
            int kVal = Math.round(kValue * 100);

            return String.format("CMYK: (%d, %d, %d, %d)", cValue, mValue, yValue, kVal);
        });
    }


}
