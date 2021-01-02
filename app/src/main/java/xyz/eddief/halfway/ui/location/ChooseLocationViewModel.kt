package xyz.eddief.halfway.ui.location

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.eddief.halfway.data.models.LocationObject
import xyz.eddief.halfway.data.repository.MapsRepository
import xyz.eddief.halfway.data.repository.UserRepository
import xyz.eddief.halfway.ui.main.home.LocationProfile

class ChooseLocationViewModel @ViewModelInject constructor(
    private val mapsRepository: MapsRepository,
    private val userRepository: UserRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
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