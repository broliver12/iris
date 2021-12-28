package com.strasz.iris.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginRight
import com.strasz.iris.R


class CornerOverlayView(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val transparentPaint = Paint()
    private val borderPaint = Paint()

    private var borderWidthDp: Float = 0f

    private val clipPath = Path()

    private var innerCorners: Float = dpToPx(24f)
    private var pxCorners: Float = dpToPx(24f)

    init {
        getResources(attrs)
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_HARDWARE, null)
        clipPath.fillType = Path.FillType.WINDING
        transparentPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        // Add colors to paint
        // fix border width
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // Draw Border
        canvas?.drawRoundRect(
            RectF(
                left.toFloat(),
                top.toFloat(),
                right.toFloat() - marginRight,
                bottom.toFloat() - marginBottom
            ), pxCorners, pxCorners, borderPaint
        )
        // Draw Hole
        canvas?.drawRoundRect(
            RectF(
                left.toFloat() + dpToPx(borderWidthDp),
                top.toFloat() + dpToPx(borderWidthDp),
                right.toFloat() - marginRight - dpToPx(borderWidthDp),
                bottom.toFloat() - marginBottom - dpToPx(borderWidthDp)
            ), innerCorners, innerCorners, transparentPaint
        )
    }

    private fun dpToPx(dp: Float) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    )

    fun getResources(attrs: AttributeSet?) {
        //Look for attributes declared in XML
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CornerOverlayView,
            0, 0
        ).apply {
            try {
                val borderW =
                    getInt(R.styleable.CornerOverlayView_borderWidth, 0)

                val borderColor =
                    getInt(R.styleable.CornerOverlayView_borderColor, Color.BLACK)

                val radius =
                    getInt(R.styleable.CornerOverlayView_outerCornerRadius, 24)
                borderWidthDp = borderW.toFloat()
                pxCorners = dpToPx(radius.toFloat())
                innerCorners = dpToPx(radius.toFloat() - borderWidthDp / 2)
                borderPaint.color = borderColor
            } finally {
                recycle()
            }
        }
    }
}