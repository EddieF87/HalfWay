package xyz.eddief.halfway.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val lastLocationId: String,
//    @ColumnInfo(name = "friend_ids") val friendIds: List<String>,
//    @ColumnInfo(name = "recent_place_ids") val recentPlaceIds: String,
) {
    val fullName get() = "$firstName $lastName"
}

@Entity
data class UserLocationUpdate(
    @PrimaryKey val userId: String,
    val lastLocationId: String
)