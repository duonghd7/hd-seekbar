package com.hdd.seekbar;

/**
 * Create on 2019-12-15
 *
 * @author duonghd
 */

public interface DSeekFunction {

    DSeekBar setTotalDuration(int totalDuration);

    DSeekBar setDuration(int duration);

    DSeekBar setDSeekListener(DSeekBar.DSeekListener dSeekListener);

    int getTotalDuration();

    int getDuration();
}
