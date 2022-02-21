package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var valueAnimator = ValueAnimator()
    private var loading = 0
    private var loadingColor: Int = context.getColor(R.color.purple)
    private var downloadColor: Int = context.getColor(R.color.violet)
    private var circleColor: Int = context.getColor(R.color.fuchsia)
    private var textColor: Int = context.getColor(R.color.blue)
    private var downloadText = "Download"
    private var loadingText = "Loading"
    private var progress: Float = 0f
    var progressVal = progress * 360f

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Loading -> {
                valueAnimator = ValueAnimator.ofInt(0, measuredWidth).apply {
                    duration = 2500
                    addUpdateListener {
                        loading = it.animatedValue as Int
                        invalidate()
                    }
                    start()

                }
            }
            ButtonState.Completed -> {
                invalidate()
            }
        }

    }


    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            loadingColor = getColor(R.styleable.LoadingButton_loadingColor, loadingColor)
            downloadColor = getColor(R.styleable.LoadingButton_downloadColor, downloadColor)
            circleColor = getColor(R.styleable.LoadingButton_circleColor, circleColor)
            textColor = getColor(R.styleable.LoadingButton_textColor, textColor)
        }

    }

    val paint = Paint().apply {
        style = Paint.Style.FILL
        textSize = 50.0f
        textAlign = Paint.Align.CENTER
        color = Color.GREEN
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = downloadColor
        if (canvas != null) {
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        }

        paint.color = loadingColor
        if (canvas != null) {
            canvas.drawRect(0f, 0f, loading.toFloat(), height.toFloat(), paint)
        }

        paint.color = textColor
        if (buttonState == ButtonState.Completed) {
            if (canvas != null) {
                canvas.drawText(downloadText, width / 2f, (height + 30) / 2f, paint)
            }
        } else {
            if (canvas != null) {
                canvas.drawText(loadingText, width / 2f, (height + 30) / 2f, paint)
            }
            paint.color = circleColor
            canvas?.drawArc(
                width / 1.2f,
                heightSize / 4f,
                (width / 1.2f + 80f),
                (heightSize / 4f) + 80f,
                0f,
                loading.toFloat(),
                true,
                paint
            )
        }

    }

    fun setState(buttonState: ButtonState) {
        this.buttonState = buttonState
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)

    }

}