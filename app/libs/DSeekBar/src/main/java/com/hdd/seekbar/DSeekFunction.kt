package com.hdd.seekbar

/**
 * Create on 2/4/21
 * @author duonghd
 */

interface DSeekFunction {
    fun setTotalDuration(totalDuration: Long): DSeekBar

    fun setDuration(duration: Long): DSeekBar

    fun setDSeekListener(dSeekListener: DSeekListener): DSeekBar

    fun getTotalDuration(): Long

    fun getDuration(): Long
}