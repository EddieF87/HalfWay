package xyz.eddief.halfway.ui.main.home

import android.location.Location
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
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
import xyz.eddief.halfway.data.repository.MapsRepository
import xyz.eddief.halfway.utils.dLog
import xyz.eddief.halfway.utils.toPlaceValue

class HomeViewModel @ViewModelInject constructor(
    private val mapsRepository: MapsRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _homeDataState = MutableLiveData<SingleEvent<HomeDataState>>()
    val homeDataState: LiveData<SingleEvent<HomeDataState>>
        get() = _homeDataState

    private val _placeType = MutableLiveData("")
    val placeType: LiveData<String>
        get() = _placeType

    var openNowChecked = true

    private val location1 = TestUtils.TEST_LOC_1
    private val location2 = TestUtils.TEST_LOC_2
    private val location3 = TestUtils.TEST_LOC_3

    fun coordinate() {
        val latLngBounds = LatLngBounds.builder()
            .include(location1.location)
            .include(location2.location)
            .include(location3.location)
            .build()

        val center = latLngBounds.center

        val arr = FloatArray(1)
        Location.distanceBetween(
            location1.location.latitude,
            location1.location.longitude,
            location2.location.latitude,
            location2.location.longitude,
            arr
        )
        fetchNearbyPlaces(center)
    }

    private fun fetchNearbyPlaces(center: LatLng) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                _homeDataState.value = SingleEvent(HomeDataState.Loading)
                _homeDataState.value = SingleEvent(
                    HomeDataState.Ready(
                        MapData(
                            locations = listOf(location1, location2, location3),
                            centerLocation = LocationObject("CENTER", center),
                            nearbyPlacesResult = getNearbyPlaces(center)
                        )
                    )
                )
            } catch (e: Exception) {
                dLog("Exception = $e")
                _homeDataState.value = SingleEvent(HomeDataState.Error(e.message))
            }
        }
    }

    private suspend fun getNearbyPlaces(location: LatLng): NearbyPlacesResult =
        withContext(Dispatchers.IO) {
            mapsRepository.getNearbyPlaces(
                location = location.toPlaceValue(),
                placeType = placeType.value ?: "",
                openNow = openNowChecked
            )
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

    fun updatePlaceType(type: String) {
        _placeType.value = type
    }
}