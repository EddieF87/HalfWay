package xyz.eddief.halfway

import android.app.Application
import xyz.eddief.halfway.data.service.NetworkService
import xyz.eddief.halfway.data.service.UserService

class HalfWayApp : Application() {

    val networkService by lazy { NetworkService(this) }
    val userService by lazy { UserService(networkService) }
}