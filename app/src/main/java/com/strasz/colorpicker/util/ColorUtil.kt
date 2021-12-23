package com.strasz.colorpicker.util

import android.graphics.Color
import kotlin.math.min
import kotlin.math.round

abstract class ColorUtil {
    companion object {
        fun generateRGBString(colorInput: Int): String {
            val redValue = Color.red(colorInput)
            val blueValue = Color.blue(colorInput)
            val greenValue = Color.green(colorInput)

            return String.format("RGB: [%d, %d, %d]", redValue, greenValue, blueValue)
        }

        fun generateHexString(colorInput: Int): String {
            val redValue = Color.red(colorInput)
            val blueValue = Color.blue(colorInput)
            val greenValue = Color.green(colorInput)

            return String.format("HEX: #%02X%02X%02X", redValue, greenValue, blueValue)
        }

        fun generateCMYKString(colorInput: Int): String {
            val redValue = Color.red(colorInput).toFloat()
            val blueValue = Color.blue(colorInput).toFloat()
            val greenValue = Color.green(colorInput).toFloat()

            var cyanValue = 1 - redValue / 255
            var magentaValue = 1 - greenValue / 255
            var yellowValue = 1 - blueValue / 255

            val kValue = min(min(cyanValue, magentaValue), yellowValue)
            if (kValue != 1f) {
                val sValue = 1 - kValue
                cyanValue = (cyanValue - kValue) / sValue
                magentaValue = (magentaValue - kValue) / sValue
                yellowValue = (yellowValue - kValue) / sValue
            }

            val cValue = round(cyanValue * 100).toInt()
            val mValue = round(magentaValue * 100).toInt()
            val yValue = round(yellowValue * 100).toInt()
            val kVal = round(kValue * 100).toInt()

            return String.format("CMYK: [%d, %d, %d, %d]", cValue, mValue, yValue, kVal)
        }
    }
}