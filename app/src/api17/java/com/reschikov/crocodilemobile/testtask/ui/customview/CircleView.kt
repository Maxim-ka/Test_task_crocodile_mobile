package com.reschikov.crocodilemobile.testtask.ui.customview

import android.content.Context
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.reschikov.crocodilemobile.testtask.R

private const val NAN = -1
private const val NAN_INT = 0
private const val NAN_FLOAT = 0.0f
private const val HALF = 0.5f

class CircleView constructor(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    init {
        initAttr(context, attrs)
        init()
    }

    private var cvStrokeWidth : Float = 0.0f
    private var strokeColor : Int = 0
    private var backDrawable : Drawable? = null
    private var radius : Float = 0.0f
    private var centerX : Float = 0.0f
    private var centerY : Float = 0.0f
    private lateinit var strokePaint : Paint

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.CircleView, NAN_INT, NAN_INT).run {
            val resId = getResourceId(R.styleable.CircleView_cv_backgroundDrawable, NAN)
            if (resId != NAN)  backDrawable = AppCompatResources.getDrawable(context, resId)
            backDrawable?.let {
                it.bounds = Rect()
            }
            cvStrokeWidth = getDimension(R.styleable.CircleView_cv_widthStroke, NAN_FLOAT)
            strokeColor = getColor(R.styleable.CircleView_cv_strokeColor, Color.BLACK)
            recycle()
        }
    }

    private fun init() {
        strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = strokeColor
            style = Paint.Style.STROKE
            strokeWidth = if (resources.configuration.orientation == ORIENTATION_PORTRAIT) cvStrokeWidth
                else cvStrokeWidth * HALF
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = 0
        var height = 0
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec)
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            height = MeasureSpec.getSize(heightMeasureSpec)
        }
        if (width > 0 && height > 0){
            val  size = if (width < height) width else height
            radius = size  * HALF
            setMeasuredDimension(size, size)
        } else super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout (changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        centerX = (right - left) * HALF
        centerY = (bottom - top) * HALF
        val half = radius * HALF
        backDrawable?.bounds?.set((centerX - half).toInt(), (centerY - half).toInt(), (centerX + half).toInt(), (centerY + half).toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        backDrawable?.draw(canvas)
        canvas.drawCircle(centerX, centerY, radius - strokePaint.strokeWidth, strokePaint)
    }
}