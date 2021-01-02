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
import xyz.eddief.halfway.data.models.*
import xyz.eddief.halfway.data.repository.MapsRepository
import xyz.eddief.halfway.data.repository.UserRepository
import xyz.eddief.halfway.utils.LocationUtils
import xyz.eddief.halfway.utils.dLog
import xyz.eddief.halfway.utils.toPlaceValue

class HomeViewModel @ViewModelInject constructor(
    private val mapsRepository: MapsRepository,
    private val userRepository: UserRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _homeDataState = MutableLiveData<SingleEvent<HomeDataState>>()
    val homeDataState: LiveData<SingleEvent<HomeDataState>>
        get() = _homeDataState

    private val _placeType = MutableLiveData("")
    val placeType: LiveData<String>
        get() = _placeType

    private val otherLocationProfiles: LiveData<List<UserWithLocations>> =
        userRepository.observeOthersWithLocations()

    private val myLocationProfile: LiveData<UserWithLocations?> =
        userRepository.observeUserWithLocations()

    var allLocationProfiles =
        ZipLiveData(myLocationProfile, otherLocationProfiles) { myProfile, otherProfiles ->
            Pair(myProfile, otherProfiles ?: emptyList())
        }

    var openNowChecked = true

    private val listOfLocations: List<LocationObject>
        get() = listOfNotNull(
            listOfNotNull(myLocationProfile.value?.currentLocation),
            otherLocationProfiles.value?.map { it.currentLocation }
        ).flatten().filterNotNull()

    fun coordinate() {
        val latLngBounds = LatLngBounds.builder()
            .also { builder ->
                listOfLocations.forEach {
                    builder.include(it.latLng)
                }
            }
            .build()
        fetchNearbyPlaces(latLngBounds.center)
    }

    private fun fetchNearbyPlaces(center: LatLng) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                _homeDataState.value = SingleEvent(HomeDataState.Loading)
                _homeDataState.value = SingleEvent(
                    HomeDataState.Ready(
                        MapData(
                            locations = listOfLocations,
                            centerLocation = LocationObject(
                                "99",
                                "CENTER",
                                center.latitude,
                                center.longitude
                            ),
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

    fun updateLocations(geoCoder: Geocoder, profile: LocationProfile, latLng: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            val location = LocationUtils.createLocationObjectFromLatLng(geoCoder, latLng)
            userRepository.updateLocationFromProfile(profile, location)
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