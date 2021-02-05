package com.hdd.seekbar

/**
 * Create on 2/4/21
 * @author duonghd
 */

interface DSeekListener {
    fun onChange(duration: Long, totalDuration: Long, percent: Float, text: String, isFocus: Boolean)

    fun onError(error: String)
}