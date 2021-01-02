package xyz.eddief.halfway.ui.main.home

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import xyz.eddief.halfway.R
import xyz.eddief.halfway.data.models.UserWithLocations


class HomeLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = 6f
        color = Color.BLACK
        pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)
    }

    private var reversedLine = false
    private val startX get() = if (reversedLine) width.toFloat() else 0f
    private val startY get() = 0f
    private val stopX get() = if (reversedLine) 0f else width.toFloat()
    private val stopY get() = height.toFloat()

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.HomeLineView,
            0, 0
        ).apply {
            try {
                reversedLine = getBoolean(R.styleable.HomeLineView_reverseLine, false)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(startX, startY, stopX, stopY, paint)
    }

    fun setLine(userWithLocations: UserWithLocations?) {
        if (userWithLocations?.user == null) {
            visibility = GONE
            return
        }
        visibility = VISIBLE
        paint.color = if (userWithLocations.hasLocation) Color.BLACK else Color.LTGRAY
    }
}