package com.hdd.seekbar

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat

/**
 * Create on 2/4/21
 * @author duonghd
 */

class DSeekBar : FrameLayout, DSeekFunction {
    private var dProgressHeight = 0
    private var dProgressColor = 0
    private var dProgressLoadedColor = 0

    private var dThumbHeight = 0
    private var dThumbWidth = 0
    private var dThumbColor = 0
    private var dThumbTextSize = 0
    private var dThumbTextColor = 0

    private var dThumbTopMargin = 0
    private var dShowTopThumb = false

    private var seekWidth = 0
    private var seekHeight = 0

    private var dSeekListener: DSeekListener? = null

    constructor(context: Context) : super(context) {
        initDSeekBar(context = context, attrs = null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initDSeekBar(context = context, attrs = attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initDSeekBar(context = context, attrs = attrs)
    }

    private fun initDSeekBar(context: Context, attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.DSeekBar, 0, 0)

        dProgressHeight = typedArray.getDimension(R.styleable.DSeekBar_dProgressHeight, context.resources.getDimension(R.dimen.dProgressHeight)).toInt()
        dThumbHeight = typedArray.getDimension(R.styleable.DSeekBar_dThumbHeight, context.resources.getDimension(R.dimen.dThumbHeight)).toInt()
        dThumbWidth = typedArray.getDimension(R.styleable.DSeekBar_dThumbWidth, context.resources.getDimension(R.dimen.dThumbWidth)).toInt()
        dThumbTextSize = typedArray.getDimension(R.styleable.DSeekBar_dThumbTextSize, context.resources.getDimension(R.dimen.dThumbTextSize)).toInt()
        dThumbTopMargin = typedArray.getDimension(R.styleable.DSeekBar_dThumbTopMargin, context.resources.getDimension(R.dimen.dThumbTopMargin)).toInt()

        dProgressColor = typedArray.getColor(R.styleable.DSeekBar_dProgressColor, ContextCompat.getColor(context, R.color.bg_progress))
        dProgressLoadedColor = typedArray.getColor(R.styleable.DSeekBar_dProgressLoadedColor, ContextCompat.getColor(context, R.color.bg_progress_loaded))
        dThumbColor = typedArray.getColor(R.styleable.DSeekBar_dThumbColor, ContextCompat.getColor(context, R.color.bg_thumbnail))
        dThumbTextColor = typedArray.getColor(R.styleable.DSeekBar_dThumbTextColor, ContextCompat.getColor(context, R.color.text_thumb_color))

        dShowTopThumb = typedArray.getBoolean(R.styleable.DSeekBar_dShowTopThumb, false)

        renderView()
    }

    private lateinit var vLoadedProgress: View
    private lateinit var vTouchView: View
    private lateinit var tvThumb: TextView
    private lateinit var tvTopThumb: TextView
    private val scaleTopThumb = 1.5f

    private fun renderView() {
        removeAllViews()

        val llRoot = LinearLayout(context)
        llRoot.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        llRoot.orientation = LinearLayout.VERTICAL

        tvTopThumb = TextView(context)
        val tvTopThumbLayoutParam = LinearLayout.LayoutParams((dThumbWidth * scaleTopThumb).toInt(), (dThumbHeight * scaleTopThumb).toInt())
        tvTopThumbLayoutParam.setMargins(0, 0, 0, dThumbTopMargin)
        tvTopThumb.layoutParams = tvTopThumbLayoutParam
        LibUtils.replaceBackgroundColor(context = context, view = tvTopThumb, color = dThumbColor)
        tvTopThumb.setTextColor(dThumbTextColor)
        tvTopThumb.setTextSize(TypedValue.COMPLEX_UNIT_DIP, LibUtils.pxToDp(context, dThumbTextSize) * scaleTopThumb)
        tvTopThumb.gravity = Gravity.CENTER
        tvTopThumb.alpha = 0f
        tvTopThumb.visibility = if (dShowTopThumb) VISIBLE else GONE

        /**
         * add TopThumb to Linear rootView -->
         * */
        llRoot.addView(tvTopThumb)
        /**
         * <----------------------------------
         * */

        val constraintProgressID = 1001
        val llProgressID = 1002
        val tvThumbID = 1003
        val vTouchViewID = 1004

        val constraintProgress = ConstraintLayout(context)
        val flProgressLayoutParam = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        constraintProgress.layoutParams = flProgressLayoutParam
        constraintProgress.id = constraintProgressID

        val llProgress = LinearLayout(context)
        llProgress.layoutParams = ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, dProgressHeight)
        llProgress.orientation = LinearLayout.HORIZONTAL
        llProgress.id = llProgressID

        vLoadedProgress = View(context)
        vLoadedProgress.layoutParams = LinearLayout.LayoutParams(dThumbWidth / 2, LayoutParams.MATCH_PARENT)
        LibUtils.replaceBackgroundColor(context = context, view = vLoadedProgress, color = dProgressLoadedColor)
        llProgress.addView(vLoadedProgress)

        val vProgress = View(context)
        vProgress.layoutParams = LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
        LibUtils.replaceBackgroundColor(context = context, view = vProgress, color = dProgressColor)
        llProgress.addView(vProgress)

        constraintProgress.addView(llProgress)

        tvThumb = TextView(context)
        tvThumb.layoutParams = ConstraintLayout.LayoutParams(dThumbWidth, dThumbHeight)
        LibUtils.replaceBackgroundColor(context = context, view = tvThumb, color = dThumbColor)
        tvThumb.setTextColor(dThumbTextColor)
        tvThumb.setTextSize(TypedValue.COMPLEX_UNIT_DIP, LibUtils.pxToDp(context = context, px = dThumbTextSize))
        tvThumb.id = tvThumbID
        tvThumb.gravity = Gravity.CENTER
        constraintProgress.addView(tvThumb)

        vTouchView = View(context)
        vTouchView.layoutParams = ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, dThumbHeight)
        vTouchView.id = vTouchViewID
        constraintProgress.addView(vTouchView)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintProgress)
        constraintSet.connect(llProgressID, ConstraintSet.BOTTOM, llProgressID, ConstraintSet.BOTTOM, 0)
        constraintSet.connect(llProgressID, ConstraintSet.TOP, llProgressID, ConstraintSet.TOP, 0)

        constraintSet.connect(tvThumbID, ConstraintSet.BOTTOM, llProgressID, ConstraintSet.BOTTOM, 0)
        constraintSet.connect(tvThumbID, ConstraintSet.TOP, llProgressID, ConstraintSet.TOP, 0)

        constraintSet.connect(vTouchViewID, ConstraintSet.BOTTOM, llProgressID, ConstraintSet.BOTTOM, 0)
        constraintSet.connect(vTouchViewID, ConstraintSet.TOP, llProgressID, ConstraintSet.TOP, 0)

        constraintSet.applyTo(constraintProgress)

        /**
         * add FlProgress to Linear rootView -->
         * */
        llRoot.addView(constraintProgress)
        /**
         * <------------------------------------
         * */

        this.addView(llRoot)
    }

    private var isFirstMeasure = true
    private var totalDuration = 0L
    private var duration = 0L
    private var xPercent = 0f
    private var isFocus = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        seekWidth = MeasureSpec.getSize(widthMeasureSpec)
        seekHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (isFirstMeasure) {
            isFirstMeasure = false
            handlerTouchView(vTouchView = vTouchView)
            update()
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handlerTouchView(vTouchView: View) {
        vTouchView.setOnTouchListener { _, motionEvent ->
            var x = motionEvent.x.toInt()
            x = when {
                x < dThumbWidth / 2 -> {
                    0
                }
                x > seekWidth - dThumbWidth / 2 -> {
                    seekWidth - dThumbWidth
                }
                else -> {
                    x - dThumbWidth / 2
                }
            }
            isFocus = motionEvent.action != MotionEvent.ACTION_UP

            when (motionEvent.action) {

                MotionEvent.ACTION_DOWN -> when {
                    totalDuration < 0 -> {
                        dSeekListener?.onError("Total duration is incorrect!")
                    }
                    totalDuration == 0L -> {
                        dSeekListener?.onError("Total duration is 0!")
                    }
                    else -> {
                        tvTopThumb.animate().alpha(1f).duration = 120
                        xPercent = 1f * x / (seekWidth - dThumbWidth)
                        duration = (xPercent * totalDuration).toLong()
                        dSeekListener?.onChange(
                            duration = duration,
                            totalDuration = totalDuration,
                            percent = xPercent,
                            text = tvThumb.text.toString(),
                            isFocus = isFocus
                        )
                        updateThumbX(x = x)
                    }
                }

                MotionEvent.ACTION_MOVE -> when {
                    totalDuration < 0 -> {
                        dSeekListener?.onError("Total duration is incorrect!")
                    }
                    totalDuration == 0L -> {
                        dSeekListener?.onError("Total duration is 0!")
                    }
                    else -> {
                        xPercent = 1f * x / (seekWidth - dThumbWidth)
                        duration = (xPercent * totalDuration).toLong()
                        dSeekListener?.onChange(
                            duration = duration,
                            totalDuration = totalDuration,
                            percent = xPercent,
                            text = tvThumb.text.toString(),
                            isFocus = isFocus
                        )
                        updateThumbX(x = x)
                    }
                }

                MotionEvent.ACTION_UP -> when {
                    totalDuration < 0 -> {
                        dSeekListener?.onError("Total duration is incorrect!")
                    }
                    totalDuration == 0L -> {
                        dSeekListener?.onError("Total duration is 0!")
                    }
                    else -> {
                        xPercent = 1f * x / (seekWidth - dThumbWidth)
                        duration = (xPercent * totalDuration).toLong()
                        tvTopThumb.animate().alpha(0f).duration = 120
                        dSeekListener?.onChange(
                            duration = duration,
                            totalDuration = totalDuration,
                            percent = xPercent,
                            text = tvThumb.text.toString(),
                            isFocus = isFocus
                        )
                        updateThumbX(x = x)
                    }
                }
            }
            motionEvent.action != MotionEvent.ACTION_UP
        }
    }

    private fun update() {
        when {
            totalDuration < 0 -> {
                dSeekListener?.onError("Total duration is incorrect!")
            }
            totalDuration == 0L -> {
                dSeekListener?.onError("Total duration is 0!")
            }
            duration > totalDuration -> {
                xPercent = 1f
                duration = totalDuration
                updateThumbX((xPercent * (seekWidth - dThumbWidth)).toInt())
            }
            else -> {
                xPercent = 1f * duration / totalDuration
                updateThumbX((xPercent * (seekWidth - dThumbWidth)).toInt())
            }
        }
    }

    private fun updateThumbX(x: Int) {
        val layoutParams = vLoadedProgress.layoutParams
        layoutParams.width = x + dThumbWidth / 2
        vLoadedProgress.layoutParams = layoutParams

        val topX: Int
        val termX = ((dThumbWidth * scaleTopThumb).toInt() - dThumbWidth) / 2
        topX = when {
            x < termX -> {
                0
            }
            x > seekWidth - tvTopThumb.width + termX -> {
                seekWidth - tvTopThumb.width
            }
            else -> {
                x - termX
            }
        }

        val text = "${LibUtils.getTime(duration = duration)} / ${LibUtils.getTime(duration = totalDuration)}"
        tvThumb.text = text
        tvTopThumb.text = text

        tvThumb.translationX = x.toFloat()
        tvTopThumb.translationX = topX.toFloat()
    }

    override fun setTotalDuration(totalDuration: Long): DSeekBar {
        this.totalDuration = totalDuration
        return this
    }

    override fun setDuration(duration: Long): DSeekBar {
        this.duration = duration
        if (!isFirstMeasure && !isFocus) {
            update()
        }
        return this
    }

    override fun setDSeekListener(dSeekListener: DSeekListener): DSeekBar {
        this.dSeekListener = dSeekListener
        return this
    }

    override fun getTotalDuration(): Long {
        return totalDuration
    }

    override fun getDuration(): Long {
        return duration
    }
}