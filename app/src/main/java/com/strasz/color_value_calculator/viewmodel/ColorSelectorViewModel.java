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

        xValueObservable = imageClicked.map(event -> (int) event.getX() - view.getXval());
        yValueObservable = imageClicked.map(event -> (int) event.getY() - view.getYval());


//        xValueObservable.subscribe(x -> System.out.println("__test X VAL: " + x.toString()));
//        yValueObservable.subscribe(x -> System.out.println("__test Y VAL: " + x.toString()));
        pixelObservable = imageClicked.map(event -> view.getCurrentImageBitmap().getPixel((int) event.getX() - view.getXval(), (int) event.getY() - view.getYval()));
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

            return "RGB: (" + redValue + ", " + greenValue + ", " + blueValue + ")";
        });

    }


}
