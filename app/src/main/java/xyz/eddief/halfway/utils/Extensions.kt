package xyz.eddief.halfway.utils

import android.util.Log

fun dLog(log: String, tag: String = DEBUG_TAG) {
    val maxLogSize = 1000
    for (i in 0..log.length / maxLogSize) {
        val start = i * maxLogSize
        var end = (i + 1) * maxLogSize
        end = if (end > log.length) log.length else end
        Log.d(tag, log.substring(start, end))
    }
}