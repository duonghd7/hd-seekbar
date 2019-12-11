package com.hdd.seekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class DSeekBar extends FrameLayout {
    private int dProgressHeight;
    private int dProgressColor;
    private int dProgressLoadedColor;

    private int dThumbHeight;
    private int dThumbWidth;
    private int dThumbColor;
    private int dThumbTextSize;
    private int dThumbTextColor;

    private boolean dShowTopThumb;

    private int seekWidth;
    private int seekHeight;

    public DSeekBar(@NonNull Context context) {
        super(context);
        initDSeekBar(context, null);
    }

    public DSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDSeekBar(context, attrs);
    }

    public DSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDSeekBar(context, attrs);
    }

    public DSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initDSeekBar(context, attrs);
    }

    private void initDSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DSeekBar, 0, 0);

        dProgressHeight = (int) typedArray.getDimension(R.styleable.DSeekBar_dProgressHeight,
                context.getResources().getDimension(R.dimen.dProgressHeight));
        dThumbHeight = (int) typedArray.getDimension(R.styleable.DSeekBar_dThumbHeight,
                context.getResources().getDimension(R.dimen.dThumbHeight));
        dThumbWidth = (int) typedArray.getDimension(R.styleable.DSeekBar_dThumbWidth,
                context.getResources().getDimension(R.dimen.dThumbWidth));
        dThumbTextSize = (int) typedArray.getDimension(R.styleable.DSeekBar_dThumbTextSize,
                context.getResources().getDimension(R.dimen.dThumbTextSize));

        dProgressColor = typedArray.getColor(R.styleable.DSeekBar_dProgressColor,
                context.getResources().getColor(R.color.bg_progress));
        dProgressLoadedColor = typedArray.getColor(R.styleable.DSeekBar_dProgressLoadedColor,
                context.getResources().getColor(R.color.bg_progress_loaded));
        dThumbColor = typedArray.getColor(R.styleable.DSeekBar_dThumbColor,
                context.getResources().getColor(R.color.bg_thumbnail));
        dThumbTextColor = typedArray.getColor(R.styleable.DSeekBar_dThumbTextColor,
                context.getResources().getColor(R.color.text_thumb_color));

        dShowTopThumb = typedArray.getBoolean(R.styleable.DSeekBar_dShowTopThumb, false);

        renderView();
    }

    private View vLoadedProgress;
    private View vTouchView;

    private void renderView() {
        this.removeAllViews();
        Context context = this.getContext();
        LinearLayout llRoot = new LinearLayout(context);
        llRoot.setLayoutParams(new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        llRoot.setOrientation(LinearLayout.VERTICAL);

        TextView tvTopThumb = new TextView(context);
        LinearLayout.LayoutParams tvTopThumbLayoutParam =
                new LinearLayout.LayoutParams((int) (dThumbWidth * 1.5), (int) (dThumbHeight * 1.5));
        tvTopThumbLayoutParam.setMargins(0, 0, 0, LibUtils.dpToPx(context, 50f));
        tvTopThumb.setLayoutParams(tvTopThumbLayoutParam);
        tvTopThumb.setBackgroundColor(dThumbColor);
        /*
         * add TopThumb to Linear rootView
         * */
        llRoot.addView(tvTopThumb);
        /**/

        int constraintProgressID = 1001;
        int llProgressID = 1002;
        int tvThumbID = 1003;
        int vTouchViewID = 1004;


        ConstraintLayout constraintProgress = new ConstraintLayout(context);
        LinearLayout.LayoutParams flProgressLayoutParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        constraintProgress.setLayoutParams(flProgressLayoutParam);
        constraintProgress.setId(constraintProgressID);

        LinearLayout llProgress = new LinearLayout(context);
        llProgress.setLayoutParams(new ConstraintLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, dProgressHeight));
        llProgress.setOrientation(LinearLayout.HORIZONTAL);
        llProgress.setId(llProgressID);

        vLoadedProgress = new View(context);
        vLoadedProgress.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT));
        vLoadedProgress.setBackgroundColor(dProgressLoadedColor);
        llProgress.addView(vLoadedProgress);

        View vProgress = new View(context);
        vProgress.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));
        vProgress.setBackgroundColor(dProgressColor);
        llProgress.addView(vProgress);

        constraintProgress.addView(llProgress);

        TextView tvThumb = new TextView(context);
        tvThumb.setLayoutParams(new ConstraintLayout.LayoutParams(dThumbWidth, dThumbHeight));
        tvThumb.setBackgroundColor(dThumbColor);
        tvThumb.setId(tvThumbID);
        constraintProgress.addView(tvThumb);

        vTouchView = new View(context);
        vTouchView.setLayoutParams(new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, LibUtils.dpToPx(context, 50)));
        vTouchView.setId(vTouchViewID);
        vTouchView.setBackgroundColor(Color.parseColor("#1A3F51B5"));
        constraintProgress.addView(vTouchView);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintProgress);
        constraintSet.connect(llProgressID, ConstraintSet.BOTTOM, llProgressID, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(llProgressID, ConstraintSet.TOP, llProgressID, ConstraintSet.TOP, 0);

        constraintSet.connect(tvThumbID, ConstraintSet.BOTTOM, llProgressID, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(tvThumbID, ConstraintSet.TOP, llProgressID, ConstraintSet.TOP, 0);

        constraintSet.connect(vTouchViewID, ConstraintSet.BOTTOM, llProgressID, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(vTouchViewID, ConstraintSet.TOP, llProgressID, ConstraintSet.TOP, 0);

        constraintSet.applyTo(constraintProgress);

        /*
         * add FlProgress to Linear rootView
         * */
        llRoot.addView(constraintProgress);
        /**/
        this.addView(llRoot);
    }

    private boolean isFirstMeasure = true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        seekWidth = MeasureSpec.getSize(widthMeasureSpec);
        seekHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (isFirstMeasure) {
            isFirstMeasure = false;
        } else {
            handlerTouchView(vTouchView);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private void handlerTouchView(View vTouchView) {
        vTouchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x = (int) motionEvent.getX();
                if (x < 0) {
                    x = 0;
                } else if (x > seekWidth) {
                    x = seekWidth;
                }

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("TAG", "touched down: " + x);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        Log.e("TAG", "movingX: " + x);

                        ViewGroup.LayoutParams layoutParams = vLoadedProgress.getLayoutParams();
                        layoutParams.width = x;
                        vLoadedProgress.setLayoutParams(layoutParams);

                        break;

                    case MotionEvent.ACTION_UP:
                        Log.e("TAG", "touched up: " + x);
                        break;
                }

                return motionEvent.getAction() != MotionEvent.ACTION_UP;
            }
        });
    }
}
