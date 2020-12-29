package xyz.eddief.halfway.ui.main.home

import android.location.Geocoder
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

    private val _readyToSubmit = MutableLiveData(false)
    val readyToSubmit: LiveData<Boolean>
        get() = _readyToSubmit

    private val _locationsAmount = MutableLiveData<Int>()
    val locationsAmount: LiveData<Int>
        get() = _locationsAmount

    var openNowChecked = true

    private var locationMine: LocationObject? = null
    private var locationOther1: LocationObject? = null
    private var locationOther2: LocationObject? = null
    private val listOfLocations get() = listOfNotNull(locationMine, locationOther1, locationOther2)

    fun coordinate() {
        val latLngBounds = LatLngBounds.builder()
            .also { builder ->
                listOfLocations.forEach {
                    builder.include(it.location)
                }
            }
            .build()

        val center = latLngBounds.center
        fetchNearbyPlaces(center)
    }

    private fun fetchNearbyPlaces(center: LatLng) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                _homeDataState.value = SingleEvent(HomeDataState.Loading)
                _homeDataState.value = SingleEvent(
                    HomeDataState.Ready(
                        MapData(
                            locations = listOfNotNull(locationMine, locationOther1, locationOther2),
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

    fun updateLocations(geocoder: Geocoder, latLng: LatLng, profile: LocationProfile) {
        when (profile) {
            LocationProfile.ME -> locationMine = LocationObject("MINE", latLng)
            LocationProfile.OTHER_1 -> locationOther1 = LocationObject("OTHER 1", latLng)
            LocationProfile.OTHER_2 -> locationOther2 = LocationObject("OTHER 2", latLng)
        }
        _locationsAmount.value = listOfLocations.size

        try {
            val address: String = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )[0].getAddressLine(0)

            _homeDataState.value = SingleEvent(
                HomeDataState.UpdateLocation(profile, address)
            )
        } catch (e: Exception) {
            _homeDataState.value = SingleEvent(HomeDataState.Error(e.message))
        }
    }

    fun updatePlaceType(type: String) {
        _placeType.value = type
    }


    //TODO: Unused functions - need to either implement or remove
    private fun fetchPlace(placeId: String) {
        val placeFields: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS
        )
        FetchPlaceRequest.builder("", placeFields)
    }

    private fun getDistanceBetween(location1: LatLng, location2: LatLng): Float {
        val arr = FloatArray(1)
        Location.distanceBetween(
            location1.latitude,
            location1.longitude,
            location2.latitude,
            location2.longitude,
            arr
        )
        return arr[0]
    }
}

enum class LocationProfile {
    ME, OTHER_1, OTHER_2
}