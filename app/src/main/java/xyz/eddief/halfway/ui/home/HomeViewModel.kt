package xyz.eddief.halfway.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.eddief.halfway.R
import xyz.eddief.halfway.data.TestUtils
import xyz.eddief.halfway.data.models.NearbyPlacesResult
import xyz.eddief.halfway.data.models.SingleEvent
import xyz.eddief.halfway.ui.HalfWayViewModel


class HomeViewModel(application: Application) : HalfWayViewModel(application) {

    private val _nearbyPlaces = MutableLiveData<SingleEvent<NearbyPlacesResult>>()
    val nearbyPlaces: LiveData<SingleEvent<NearbyPlacesResult>>
        get() = _nearbyPlaces

    fun fetchNearbyPlaces() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                _nearbyPlaces.value = SingleEvent(getNearbyPlaces())
            } catch (e: Exception) {
                Log.d("HalfWayDebug", "Exception = $e")
            }
        }
    }

    private suspend fun getNearbyPlaces(): NearbyPlacesResult = withContext(Dispatchers.IO) {
        app.userService.getNearbyPlaces(TestUtils.TEST_LOCATION, TestUtils.TEST_RADIUS)!!
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