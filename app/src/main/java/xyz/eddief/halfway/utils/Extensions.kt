package xyz.eddief.halfway.utils

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng

fun dLog(log: String, tag: String = DEBUG_TAG) {
    val maxLogSize = 1000
    for (i in 0..log.length / maxLogSize) {
        val start = i * maxLogSize
        var end = (i + 1) * maxLogSize
        end = if (end > log.length) log.length else end
        Log.d(tag, log.substring(start, end))
    }
}

fun LatLng.toPlaceValue() : String = "$latitude, $longitude"

fun Float.toPX(context: Context): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Float.toDP(context: Context): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}