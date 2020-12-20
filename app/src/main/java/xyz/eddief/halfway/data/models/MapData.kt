package xyz.eddief.halfway.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MapData(
    val locations: List<LocationObject>,
    val centerLocation: LocationObject,
    val nearbyPlacesResult: NearbyPlacesResult
) : Parcelable
