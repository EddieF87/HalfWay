package xyz.eddief.halfway.ui.location

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.eddief.halfway.data.models.LocationObject
import xyz.eddief.halfway.data.repository.MapsRepository
import xyz.eddief.halfway.data.repository.UserRepository
import xyz.eddief.halfway.ui.main.home.LocationProfile
import javax.inject.Inject

@HiltViewModel
class ChooseLocationViewModel @Inject constructor(
    private val mapsRepository: MapsRepository,
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var profile: LocationProfile? = null

    fun updateLocation(location: LocationObject) {
        profile?.let {
            viewModelScope.launch(Dispatchers.IO) {
                userRepository.updateLocationFromProfile(it, location)
            }
        }
    }
}