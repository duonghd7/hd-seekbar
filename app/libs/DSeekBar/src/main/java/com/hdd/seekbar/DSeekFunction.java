package com.hdd.seekbar;

/**
 * Create on 2019-12-15
 *
 * @author duonghd
 */

public interface DSeekFunction {

    void setSeek(float percent);

    void setSeek(float percent, String text);

    void setSeek(float duration, float totalDuration);

    void setSeek(float duration, float totalDuration, String text);

    void setDSeekChangeListener(DSeekBar.DSeekChangeListener dSeekChangeListener);

    void setDSeekErrorListener(DSeekBar.DSeekErrorListener dSeekErrorListener);
}
