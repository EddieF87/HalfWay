package xyz.eddief.halfway.data.models

import com.google.android.gms.maps.model.LatLng

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val lastLocation: LatLng,
    val friendIds: List<String>,
    val recentPlaceIds: String,
) {
}