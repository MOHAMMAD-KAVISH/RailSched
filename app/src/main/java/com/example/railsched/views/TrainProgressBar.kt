package com.example.railsched.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.example.railsched.R

class TrainProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var progress: Float = 0f
    private var progressAnimator: ValueAnimator? = null

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
        style = Paint.Style.FILL
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#00C853")
        style = Paint.Style.FILL
    }

    private val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.DKGRAY
        strokeWidth = 4f
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 36f
        typeface = Typeface.DEFAULT_BOLD
    }

    private var iconBitmap: Bitmap? = null

    init {
        // Load train icon bitmap from drawable
        val drawable = AppCompatResources.getDrawable(context, R.drawable.ic_train)
        drawable?.let {
            val size = 100
            val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            it.setBounds(0, 0, size, size)
            it.draw(canvas)
            iconBitmap = bmp
        }
    }

    fun animateProgressTo(target: Float, duration: Long = 2000) {
        progressAnimator?.cancel()
        progressAnimator = ValueAnimator.ofFloat(progress, target.coerceIn(0f, 1f)).apply {
            this.duration = duration
            addUpdateListener {
                progress = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        val centerY = h / 2f
        val trackHeight = h / 6f

        // Track background
        canvas.drawRoundRect(0f, centerY - trackHeight / 2, w, centerY + trackHeight / 2, 30f, 30f, trackPaint)

        // Progress bar
        val progressWidth = w * progress
        canvas.drawRoundRect(0f, centerY - trackHeight / 2, progressWidth, centerY + trackHeight / 2, 30f, 30f, progressPaint)

        // Tick marks
        val tickCount = 5
        for (i in 0..tickCount) {
            val x = i * (w / tickCount)
            canvas.drawLine(x, centerY - trackHeight, x, centerY + trackHeight, tickPaint)
        }

        // Train icon
        iconBitmap?.let {
            val iconSize = h * 0.9f
            val iconX = (progressWidth - iconSize / 2).coerceIn(0f, w - iconSize)
            val iconY = (h - iconSize) / 2
            canvas.drawBitmap(it, null, RectF(iconX, iconY, iconX + iconSize, iconY + iconSize), null)
        }

        // Percentage label
        val label = "${(progress * 100).toInt()}%"
        val labelX = (w - textPaint.measureText(label)) / 2
        val labelY = centerY + trackHeight * 2
        canvas.drawText(label, labelX, labelY, textPaint)
    }
}
