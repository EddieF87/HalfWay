package xyz.eddief.halfway.data

import com.google.android.gms.maps.model.LatLng
import xyz.eddief.halfway.data.models.LocationObject

object TestUtils {
    const val TEST_RADIUS = "5000"
    val TEST_LOC_1 = LocationObject("My Location", LatLng(-34.3, 151.0))
    val TEST_LOC_2 = LocationObject("Your Location", LatLng(-33.9, 151.0))
    val TEST_LOC_3 = LocationObject("Their Location", LatLng(-34.0, 151.3))
}