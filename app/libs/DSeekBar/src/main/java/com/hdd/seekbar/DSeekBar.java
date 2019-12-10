package com.hdd.seekbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DSeekBar extends FrameLayout {

    public DSeekBar(@NonNull Context context) {
        super(context);
        initDSeekBar(context);
    }

    public DSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDSeekBar(context);
    }

    public DSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDSeekBar(context);
    }

    public DSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initDSeekBar(context);
    }

    private void initDSeekBar(@NonNull Context context) {

    }
}
