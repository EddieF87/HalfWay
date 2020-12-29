package xyz.eddief.halfway.ui.main.home

import xyz.eddief.halfway.data.models.MapData

sealed class HomeDataState {
    class Ready(val mapData: MapData) : HomeDataState()
    class UpdateLocation(val profile: LocationProfile, val address: String) : HomeDataState()
    class Error(val error: String?) : HomeDataState()
    object Loading : HomeDataState()
}