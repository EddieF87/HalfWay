package xyz.eddief.halfway.utils

import android.view.View

fun View.fade(fadeOut: Boolean) {
    this.animate().alpha(if (fadeOut) 0f else 1f).setDuration(500).start()
}