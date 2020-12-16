package xyz.eddief.halfway.ui.home

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.eddief.halfway.data.TestUtils
import xyz.eddief.halfway.data.models.LocationObject
import xyz.eddief.halfway.data.models.MapData
import xyz.eddief.halfway.data.models.NearbyPlacesResult
import xyz.eddief.halfway.data.models.SingleEvent
import xyz.eddief.halfway.ui.HalfWayViewModel
import xyz.eddief.halfway.utils.DEBUG_TAG


class HomeViewModel(application: Application) : HalfWayViewModel(application) {

    private val _nearbyPlaces = MutableLiveData<SingleEvent<NearbyPlacesResult>>()
    val nearbyPlaces: LiveData<SingleEvent<NearbyPlacesResult>>
        get() = _nearbyPlaces

    private val _mapData = MutableLiveData<SingleEvent<MapData>>()
    val mapData: LiveData<SingleEvent<MapData>>
        get() = _mapData

    private val location1 = TestUtils.TEST_LOC_1
    private val location2 = TestUtils.TEST_LOC_2
    private val location3 = TestUtils.TEST_LOC_3
    var center: LatLng = LatLng(0.0, 0.0)

    fun coordinate() {
        val latLngBounds = LatLngBounds.builder()
            .include(location1.location)
            .include(location2.location)
            .include(location3.location)
            .build()

        center = latLngBounds.center

        val distanceBetween = Location.distanceBetween(location1.location.latitude, location1.location.longitude, location2.location.latitude, location2.location.longitude, FloatArray(4))
        Log.d(DEBUG_TAG, "distance = $distanceBetween")
        fetchNearbyPlaces(center)
    }

    private fun fetchNearbyPlaces(center: LatLng) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                _mapData.value = SingleEvent(
                    MapData(
                        listOf(location1, location2,
                            location3 ,
                            LocationObject("CENTER", center)),
                        nearbyPlacesResult = getNearbyPlaces(center)
                    )
                )
            } catch (e: Exception) {
                Log.d(DEBUG_TAG, "Exception = $e")
            }
        }
    }

    private suspend fun getNearbyPlaces(location: LatLng): NearbyPlacesResult =
        withContext(Dispatchers.IO) {
            val i =
                app.userService.getNearbyPlaces(
                    "${location.latitude}, ${location.longitude}",
                    TestUtils.TEST_RADIUS
                )!!
            i.results.forEach {
                Log.d(DEBUG_TAG, "${it.name}   ${it.types}  ${it.opening_hours?.open_now}")
            }
            return@withContext i
        }

    fun fetchPlace(placeId: String) {
        val placeFields: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS
        )
        FetchPlaceRequest.builder("", placeFields)
    }
}