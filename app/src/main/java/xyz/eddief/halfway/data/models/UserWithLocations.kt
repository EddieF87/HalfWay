package xyz.eddief.halfway.data.models

import androidx.room.Embedded
import androidx.room.Relation


data class UserWithLocations(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "lastLocationId",
        entityColumn = "locationId",
        entity = LocationObject::class
    )
    var locations: List<LocationObject> = emptyList()
) {
    val hasLocation: Boolean get() = locations.any()
    val currentLocation: LocationObject? get() = locations.firstOrNull()
}