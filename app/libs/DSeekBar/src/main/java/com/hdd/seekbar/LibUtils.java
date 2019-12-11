package com.hdd.seekbar;

import android.content.Context;
import android.util.TypedValue;

public class LibUtils {
    public static int dpToPx(Context context, float dp) {
        if (context == null) throw new RuntimeException("Context is null, init(Context)");
        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }
}
