package com.strasz.color_value_calculator.Utils;

import android.graphics.Color;

public interface ColorUtils {

    static String generateRGBString(int colorInput) {
        int redValue = Color.red(colorInput);
        int blueValue = Color.blue(colorInput);
        int greenValue = Color.green(colorInput);

        return String.format("RGB: (%d, %d, %d)", redValue, greenValue, blueValue);
    }

    static String generateHexString(int colorInput) {
        int redValue = Color.red(colorInput);
        int blueValue = Color.blue(colorInput);
        int greenValue = Color.green(colorInput);

        return String.format("HEX: #%02X%02X%02X", redValue, greenValue, blueValue);
    }

    static String generateCMYKString(int colorInput) {
        float redValue = Color.red(colorInput);
        float blueValue = Color.blue(colorInput);
        float greenValue = Color.green(colorInput);

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
    }
}
