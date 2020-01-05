package com.subham.customviewclock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import java.util.*

class ClockView : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context)

    private var viewWidth: Int = 0
    private var viewHeight: Int = 0
    private val viewPaddingBySides = 50
    private var radius = 0f
    private val smallRadius = 10
    private var fontSize = 0f

    private val paintStroke = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val paintFill = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        strokeWidth = 5f
    }

    private val paintHandle = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        strokeWidth = 7f
    }

    init {
        setBackgroundColor(Color.BLACK)
        fontSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13f, resources.displayMetrics)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w;
        viewHeight = h;
        radius = (w - (viewPaddingBySides * 2)) / 2f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(viewWidth / 2f, viewHeight / 2f, radius, paintStroke)
        canvas?.drawCircle(viewWidth / 2f, viewHeight / 2f, smallRadius.toFloat(), paintFill)
        drawNumeral(canvas)
        drawClockHandles(canvas)
        postInvalidateDelayed(1000)
    }

    private fun drawNumeral(canvas: Canvas?) {
        paintFill.textSize = fontSize

        for (number in 1..12) {
//            val number  = 12
            val tmp = number.toString()

            val angle = Math.toRadians((30 * (number - 3)).toDouble())

            val radiusToNumerics = findLengthByPercentage(85, radius)

            val x = (viewWidth / 2 + Math.cos(angle) * radiusToNumerics).toFloat()
            val y = (viewHeight / 2 + Math.sin(angle) * radiusToNumerics).toFloat()
            canvas?.drawText(tmp, x, y, paintFill)

        }

    }

    private fun findLengthByPercentage(percentage: Int, totalSize: Float): Float {

        return (totalSize / 100) * percentage

    }

    private fun drawClockHandles(canvas: Canvas?) {
        val cal = Calendar.getInstance()

        drawSecondHandle(canvas, cal)

        drawMinuteHandle(canvas, cal)

        drawHourHandle(canvas, cal)
    }

    private fun drawHourHandle(canvas: Canvas?, cal: Calendar) {

        val minute = cal.get(Calendar.HOUR)
        val degreeFromMin = getDegree(minute,true)
        val coordsFromDegree = getCoordsFromDegree(degreeFromMin - 90, 60)

        canvas?.drawLine(
            viewWidth / 2f,
            viewHeight / 2f,
            coordsFromDegree.first,
            coordsFromDegree.second,
            paintHandle
        )

    }

    private fun drawMinuteHandle(canvas: Canvas?, cal: Calendar) {
        val minute = cal.get(Calendar.MINUTE)
        val degreeFromMin = getDegree(minute,false)
        val coordsFromDegree = getCoordsFromDegree(degreeFromMin - 90, 75)

        canvas?.drawLine(
            viewWidth / 2f,
            viewHeight / 2f,
            coordsFromDegree.first,
            coordsFromDegree.second,
            paintHandle
        )

    }

    private fun drawSecondHandle(canvas: Canvas?, cal: Calendar) {

        val second = cal.get(Calendar.SECOND)
        Log.e("Second", "" + second)
        val degreeFromSec = getDegree(second, false)
        val coordsFromDegree = getCoordsFromDegree(degreeFromSec - 90, 65)

        canvas?.drawLine(
            viewWidth / 2f,
            viewHeight / 2f,
            coordsFromDegree.first,
            coordsFromDegree.second,
            paintHandle
        )

    }

    private fun getDegree(unit: Int, isHour: Boolean): Int {

        val onePercent = if (isHour) {
            360 / 12f
        } else {
            360 / 60f
        }

        val f = onePercent * unit
        return Math.round(f)
    }

    private fun getCoordsFromDegree(degree: Int, length: Int): Pair<Float, Float> {

        val angle = Math.toRadians(degree.toDouble())
        val radiusToHandle = findLengthByPercentage(length, radius)
        val x = (viewWidth / 2 + Math.cos(angle) * radiusToHandle).toFloat()
        val y = (viewHeight / 2 + Math.sin(angle) * radiusToHandle).toFloat()
        return Pair(x, y)
    }

}