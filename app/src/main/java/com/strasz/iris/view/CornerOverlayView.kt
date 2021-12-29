package com.strasz.iris.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginRight
import com.strasz.iris.R


class CornerOverlayView(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    // Create [Paint] objects
    private val clearPaint = Paint()
    private val borderPaint = Paint()

    private var borderWidthDp: Float = 0f
    private var innerCornerRadiusPx: Float = dpToPx(24f)
    private var outerCornerRadiusPx: Float = dpToPx(24f)

    private val clipPath = Path()

    private var outerRectF: RectF? = null
    private var innerRectF: RectF? = null

    init {
        parseXmlAttributes(attrs)
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_HARDWARE, null)
        clipPath.fillType = Path.FillType.WINDING
        clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (viewTreeObserver.isAlive) {
                        outerRectF = RectF(
                            left.toFloat(),
                            top.toFloat(),
                            right.toFloat() - marginRight,
                            bottom.toFloat() - marginBottom
                        )
                        innerRectF = RectF(
                            left.toFloat() + dpToPx(borderWidthDp),
                            top.toFloat() + dpToPx(borderWidthDp),
                            right.toFloat() - marginRight - dpToPx(borderWidthDp),
                            bottom.toFloat() - marginBottom - dpToPx(borderWidthDp)
                        )
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            }
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // Draw Border
        outerRectF?.let { rect ->
            canvas?.drawRoundRect(rect, outerCornerRadiusPx, outerCornerRadiusPx, borderPaint)
        }

        // Draw Hole
        innerRectF?.let { rect ->
            canvas?.drawRoundRect(rect, innerCornerRadiusPx, innerCornerRadiusPx, clearPaint)
        }
    }

    private fun dpToPx(dp: Float) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    )

    // Parse XML attributes and initialize local variables to provided (or default) values
    private fun parseXmlAttributes(attrs: AttributeSet?) {
        //Look for attributes declared in XML
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CornerOverlayView,
            0, 0
        ).apply {
            try {
                val xmlArgBorderWidth =
                    getInt(R.styleable.CornerOverlayView_borderWidth, 0)

                val xmlArgBorderColor =
                    getInt(
                        R.styleable.CornerOverlayView_borderColor,
                        ResourcesCompat.getColor(resources, R.color.black, null)
                    )

                val xmlArgBorderRadius =
                    getInt(R.styleable.CornerOverlayView_outerCornerRadius, 24)
                borderWidthDp = xmlArgBorderWidth.toFloat()
                outerCornerRadiusPx = dpToPx(xmlArgBorderRadius.toFloat())
                innerCornerRadiusPx = dpToPx(xmlArgBorderRadius.toFloat() - borderWidthDp / 2)
                borderPaint.color = xmlArgBorderColor
            } finally {
                recycle()
            }
        }
    }
}