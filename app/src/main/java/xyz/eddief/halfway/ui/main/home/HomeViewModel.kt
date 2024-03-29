package xyz.eddief.halfway.ui.main.home

import android.location.Location
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.eddief.halfway.data.models.*
import xyz.eddief.halfway.data.repository.MapsRepository
import xyz.eddief.halfway.data.repository.UserRepository
import xyz.eddief.halfway.utils.PlaceTypeUtils
import xyz.eddief.halfway.utils.SharedPreferencesController
import xyz.eddief.halfway.utils.dLog
import xyz.eddief.halfway.utils.toPlaceValue
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mapsRepository: MapsRepository,
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferencesController,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _homeDataState = MutableLiveData<SingleEvent<HomeDataState>>()
    val homeDataState: LiveData<SingleEvent<HomeDataState>>
        get() = _homeDataState

    private val placeToMeet get() = sharedPreferences.placeToMeet

    private val _placeToMeetDisplay = MutableLiveData(
        if (sharedPreferences.isSearchByKeyword) {
            placeToMeet
        } else {
            PlaceTypeUtils.getPlaceKey(placeToMeet)
        }
    )

    val placeToMeetDisplay: LiveData<String>
        get() = _placeToMeetDisplay

    private val _centerLatLng = MutableLiveData<LatLng>()
    val centerLatLng: LiveData<LatLng>
        get() = _centerLatLng

    var centerLocation: LocationObject? = null

    private var listOfLocations: List<LocationObject> = listOf()

    val allLocationProfiles: LiveData<Pair<UserWithLocations?, List<UserWithLocations>>> =
        userRepository.allSelectedWithLocationsFlow
            .onEach { pair ->
                listOfLocations = mapListOfLocations(pair.first, pair.second)
                getCenterLocation(listOfLocations)?.let {
                    _centerLatLng.value = it
                }
                dLog("uuuuuuuuuuuuu ${pair.first}  , ${pair.second}")
            }
            .asLiveData()

    var openNowChecked = true
    private val isSearchByKeyword get() = sharedPreferences.isSearchByKeyword

    private fun mapListOfLocations(
        myProfile: UserWithLocations?,
        otherProfiles: List<UserWithLocations>
    ) = listOfNotNull(
        listOfNotNull(myProfile?.currentLocation),
        otherProfiles.map { it.currentLocation }
    ).flatten().filterNotNull()

    fun getCenterLocation(locations: List<LocationObject>): LatLng? =
        locations.takeIf { it.size > 1 }?.let {
            LatLngBounds.builder()
                .also { builder ->
                    it.forEach {
                        builder.include(it.latLng())
                    }
                }
                .build()
                .center
        }

    fun fetchNearbyPlaces() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                _homeDataState.value = SingleEvent(HomeDataState.Loading)
                _homeDataState.value = SingleEvent(
                    HomeDataState.Ready(
                        MapData(
                            locations = listOfLocations,
                            centerLocation = centerLocation!!,
                            nearbyPlacesResult = getNearbyPlaces(centerLocation!!.latLng())
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
            mapsRepository.getNearbyPlacesByType(
                location = location.toPlaceValue(),
                placeToMeet = placeToMeetDisplay.value ?: "",
                isKeyword = isSearchByKeyword,
                openNow = openNowChecked
            )
        }

    fun updateLocation(profile: LocationProfile, location: LocationObject) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateLocationFromProfile(profile, location)
        }
    }

    fun updatePlaceType(place: String?, isKeyword: Boolean) {
        place?.let {
            val placeToMeet = when {
                isKeyword -> place
                else -> PlaceTypeUtils.getPlaceType(place)
            }
            _placeToMeetDisplay.value = when {
                isKeyword -> place
                PlaceTypeUtils.hasPlaceKey(place) -> place
                else -> "Anywhere"
            }
            sharedPreferences.placeToMeet = placeToMeet
            sharedPreferences.isSearchByKeyword = isKeyword
        }
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