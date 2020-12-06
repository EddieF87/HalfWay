package xyz.eddief.halfway.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import xyz.eddief.halfway.HalfWayApp


open class HalfWayViewModel(application: Application): AndroidViewModel(application) {
    val app get() = getApplication<HalfWayApp>()
}


