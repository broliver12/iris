package com.strasz.color_value_calculator.view;

import android.graphics.Bitmap;
import android.view.MotionEvent;

import io.reactivex.Observable;

public interface IColorSelectorView {

    Observable<MotionEvent> getImageClicked();

    Bitmap getCurrentImageBitmap();
}
