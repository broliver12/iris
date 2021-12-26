package com.strasz.colorpicker.util

import android.graphics.Color
import kotlin.math.min
import kotlin.math.round

abstract class ColorUtil {
    companion object {
        private val Int.r
            get() = Color.red(this)
        private val Int.g
            get() = Color.green(this)
        private val Int.b
            get() = Color.blue(this)

        fun formatRgb(colorInput: Int): String {
            return String.format("[%d, %d, %d]", colorInput.r, colorInput.g, colorInput.b)
        }

        fun formatHex(colorInput: Int): String {
            return String.format("#%02X%02X%02X", colorInput.r, colorInput.g, colorInput.b)
        }

        fun formatCmyk(colorInput: Int): String {
            var cyanValue = 1 - colorInput.r.toFloat() / 255
            var magentaValue = 1 - colorInput.b.toFloat() / 255
            var yellowValue = 1 - colorInput.g.toFloat() / 255

            val kValue = min(min(cyanValue, magentaValue), yellowValue)
            if (kValue != 1f) {
                val sValue = 1 - kValue
                cyanValue = (cyanValue - kValue) / sValue
                magentaValue = (magentaValue - kValue) / sValue
                yellowValue = (yellowValue - kValue) / sValue
            }

            return String.format("[%d, %d, %d, %d]", cyanValue.rnd(), magentaValue.rnd(), yellowValue.rnd(), kValue.rnd())
        }

        private fun Float.rnd() = round(this * 100).toInt()
    }
}