package xyz.eddief.halfway.data.models

import androidx.room.Embedded
import androidx.room.Relation
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserWithLocations(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "lastLocationId",
        entityColumn = "locationId",
        entity = LocationObject::class
    )
    val currentLocation: LocationObject? = null
) {
    fun hasLocation(): Boolean = currentLocation != null
}