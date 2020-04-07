package com.hdd.seekbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
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
import androidx.core.content.ContextCompat;

public class DSeekBar extends FrameLayout implements DSeekFunction {
    private int dProgressHeight;
    private int dProgressColor;
    private int dProgressLoadedColor;

    private int dThumbHeight;
    private int dThumbWidth;
    private int dThumbColor;
    private int dThumbTextSize;
    private int dThumbTextColor;

    private int dThumbTopMargin;

    private boolean dShowTopThumb;

    private int seekWidth;
    private int seekHeight;

    private DSeekListener dSeekListener;

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
        dThumbTopMargin = (int) typedArray.getDimension(R.styleable.DSeekBar_dThumbTopMargin,
                context.getResources().getDimension(R.dimen.dThumbTopMargin));

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
    private TextView tvThumb;
    private TextView tvTopThumb;
    private float scaleTopThumb = 1.5f;

    private void renderView() {
        this.removeAllViews();
        Context context = this.getContext();
        LinearLayout llRoot = new LinearLayout(context);
        llRoot.setLayoutParams(new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        llRoot.setOrientation(LinearLayout.VERTICAL);

        tvTopThumb = new TextView(context);
        LinearLayout.LayoutParams tvTopThumbLayoutParam =
                new LinearLayout.LayoutParams((int) (dThumbWidth * scaleTopThumb), (int) (dThumbHeight * 1.5));
        tvTopThumbLayoutParam.setMargins(0, 0, 0, dThumbTopMargin);
        tvTopThumb.setLayoutParams(tvTopThumbLayoutParam);
        replaceBackgroundColor(context, tvTopThumb, dThumbColor);
        tvTopThumb.setTextColor(dThumbTextColor);
        tvTopThumb.setTextSize(TypedValue.COMPLEX_UNIT_DIP, LibUtils.pxToDp(context, dThumbTextSize) * scaleTopThumb);
        tvTopThumb.setGravity(Gravity.CENTER);

        tvTopThumb.setAlpha(0);
        tvTopThumb.setVisibility(dShowTopThumb ? View.VISIBLE : View.GONE);
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
        vLoadedProgress.setLayoutParams(new LinearLayout.LayoutParams(dThumbWidth / 2, LayoutParams.MATCH_PARENT));
        replaceBackgroundColor(context, vLoadedProgress, dProgressLoadedColor);
        llProgress.addView(vLoadedProgress);

        View vProgress = new View(context);
        vProgress.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));
        replaceBackgroundColor(context, vProgress, dProgressColor);
        llProgress.addView(vProgress);

        constraintProgress.addView(llProgress);

        tvThumb = new TextView(context);
        tvThumb.setLayoutParams(new ConstraintLayout.LayoutParams(dThumbWidth, dThumbHeight));
        replaceBackgroundColor(context, tvThumb, dThumbColor);
        tvThumb.setTextColor(dThumbTextColor);
        tvThumb.setTextSize(TypedValue.COMPLEX_UNIT_DIP, LibUtils.pxToDp(context, dThumbTextSize));
        tvThumb.setId(tvThumbID);
        tvThumb.setGravity(Gravity.CENTER);
        constraintProgress.addView(tvThumb);

        vTouchView = new View(context);
        vTouchView.setLayoutParams(new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, LibUtils.dpToPx(context, 40)));
        vTouchView.setId(vTouchViewID);
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
    private int totalDuration = 0;
    private int duration = 0;
    private float xPercent = 0;
    private boolean isFocus = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        seekWidth = MeasureSpec.getSize(widthMeasureSpec);
        seekHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (isFirstMeasure) {
            isFirstMeasure = false;
            handlerTouchView(vTouchView);
            update();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void handlerTouchView(View vTouchView) {
        vTouchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x = (int) motionEvent.getX();
                if (x < dThumbWidth / 2) {
                    x = 0;
                } else if (x > seekWidth - dThumbWidth / 2) {
                    x = seekWidth - dThumbWidth;
                } else {
                    x = x - dThumbWidth / 2;
                }

                isFocus = motionEvent.getAction() != MotionEvent.ACTION_UP;

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (totalDuration < 0) {
                            if (dSeekListener != null) {
                                dSeekListener.onError("Total duration is incorrect!");
                            }
                        } else if (totalDuration == 0) {
                            if (dSeekListener != null) {
                                dSeekListener.onError("Total duration is 0!");
                            }
                        } else {
                            tvTopThumb.animate().alpha(1f).setDuration(120);
                            xPercent = 1f * x / (seekWidth - dThumbWidth);
                            duration = (int) (xPercent * totalDuration);
                            if (dSeekListener != null) {
                                dSeekListener.onChange(duration, totalDuration, xPercent, tvThumb.getText().toString(), isFocus);
                            }
                            updateThumbX(x);
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (totalDuration < 0) {
                            if (dSeekListener != null) {
                                dSeekListener.onError("Total duration is incorrect!");
                            }
                        } else if (totalDuration == 0) {
                            if (dSeekListener != null) {
                                dSeekListener.onError("Total duration is 0!");
                            }
                        } else {
                            xPercent = 1f * x / (seekWidth - dThumbWidth);
                            duration = (int) (xPercent * totalDuration);
                            if (dSeekListener != null) {
                                dSeekListener.onChange(duration, totalDuration, xPercent, tvThumb.getText().toString(), isFocus);
                            }
                            updateThumbX(x);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        if (totalDuration < 0) {
                            if (dSeekListener != null) {
                                dSeekListener.onError("Total duration is incorrect!");
                            }
                        } else if (totalDuration == 0) {
                            if (dSeekListener != null) {
                                dSeekListener.onError("Total duration is 0!");
                            }
                        } else {
                            xPercent = 1f * x / (seekWidth - dThumbWidth);
                            duration = (int) (xPercent * totalDuration);

                            tvTopThumb.animate().alpha(0f).setDuration(120);
                            updateThumbX(x);
                            if (dSeekListener != null) {
                                dSeekListener.onChange(duration, totalDuration, xPercent, tvThumb.getText().toString(), isFocus);
                            }
                        }
                        break;
                }
                return motionEvent.getAction() != MotionEvent.ACTION_UP;
            }
        });
    }

    private void update() {
        if (totalDuration < 0) {
            if (dSeekListener != null) {
                dSeekListener.onError("Total duration is incorrect!");
            }
        } else if (totalDuration == 0) {
            if (dSeekListener != null) {
                dSeekListener.onError("Total duration is 0!");
            }
        } else if (duration > totalDuration) {
            xPercent = 1f;
            duration = totalDuration;
            updateThumbX((int) (xPercent * (seekWidth - dThumbWidth)));
        } else {
            xPercent = 1f * duration / totalDuration;
            updateThumbX((int) (xPercent * (seekWidth - dThumbWidth)));
        }
    }

    private void updateThumbX(int x) {
        ViewGroup.LayoutParams layoutParams = vLoadedProgress.getLayoutParams();
        layoutParams.width = x + dThumbWidth / 2;
        vLoadedProgress.setLayoutParams(layoutParams);

        int topX;
        int termX = ((int) (dThumbWidth * scaleTopThumb) - dThumbWidth) / 2;
        if (x < termX) {
            topX = 0;
        } else if (x > seekWidth - tvTopThumb.getWidth() + termX) {
            topX = seekWidth - tvTopThumb.getWidth();
        } else {
            topX = x - termX;
        }

        String text = getTime(duration) + " / " + getTime(totalDuration);
        tvThumb.setText(text);
        tvTopThumb.setText(text);

        tvThumb.setTranslationX(x);
        tvTopThumb.setTranslationX(topX);
    }

    @SuppressLint("DefaultLocale")
    private String getTime(int duration) {
        int dur = duration / 1000;
        int min = dur / 60;
        int sec = dur % 60;

        return String.format("%d:%02d", min, sec);
    }

    private void replaceBackgroundColor(Context context, View view, int color) {
        Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.bg_radius);
        if (mDrawable != null) {
            mDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            view.setBackground(mDrawable);
        }
    }

    @Override
    public DSeekBar setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
        return this;
    }

    @Override
    public DSeekBar setDuration(int duration) {
        this.duration = duration;
        if (!isFirstMeasure && !isFocus) {
            update();
        }
        return this;
    }

    @Override
    public DSeekBar setDSeekListener(DSeekListener dSeekListener) {
        this.dSeekListener = dSeekListener;
        return this;
    }

    @Override
    public int getTotalDuration() {
        return totalDuration;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    public interface DSeekListener {
        void onChange(int duration, int totalDuration, float percent, String text, boolean isFocus);

        void onError(String error);
    }
}