package xyz.eddief.halfway

import android.app.Application
import com.facebook.appevents.AppEventsLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HalfWayApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppEventsLogger.activateApp(this)
    }

}