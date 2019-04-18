package com.strasz.color_value_calculator.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

public interface Utils {

//    private static int TOUCHSLOP =

    static boolean checkPermission(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    static boolean wasClick(MotionEvent down, MotionEvent up) {

        int startX = (int) down.getX();
        int startY = (int) down.getY();

        int endX = (int) up.getX();
        int endY = (int) up.getY();
        int dX = Math.abs(endX - startX);
        int dY = Math.abs(endY - startY);


        if (Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2)) <= 8) {
            return true;
        }

        return false;
    }
}
