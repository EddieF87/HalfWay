package xyz.eddief.halfway.utils

import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import xyz.eddief.halfway.data.models.LocationObject

object LocationUtils {

    fun createLocationObjectFromLatLng(geoCoder: Geocoder, latLng: LatLng): LocationObject {
        val geoCodeResults = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        val address = getAddressFromGeoCodeResults(geoCodeResults)
        val featureName = getFeatureName(geoCodeResults)

        return LocationObject(
            title = featureName,
            latitude = latLng.latitude,
            longitude = latLng.longitude,
            address = address
        )
    }

    private fun getFeatureName(results: List<Address>): String = results.firstOrNull()?.featureName ?: ""

    fun getAddressFromGeoCodeResults(results: List<Address>): String = try {
        results[0].getAddressLine(0)
    } catch (e: Exception) {
        "Unknown Address"
    }
}

