package com.hdd.seekbar

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat

/**
 * Create on 2/4/21
 * @author duonghd
 */

object LibUtils {
    fun dpToPx(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        ).toInt()
    }

    fun pxToDp(context: Context, px: Int): Float {
        return px / context.resources.displayMetrics.density
    }

    fun replaceBackgroundColor(context: Context, view: View, color: Int) {
        val mDrawable = ContextCompat.getDrawable(context, R.drawable.bg_radius)
        if (mDrawable != null) {
            mDrawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
            view.background = mDrawable
        }
    }

    fun getTime(duration: Long): String {
        val dur = duration / 1000
        val min = dur / 60
        val sec = dur % 60

        return String.format("%d:%02d", min, sec)
    }
}