package xyz.eddief.halfway.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val lastLocationId: String = "",
//    @ColumnInfo(name = "friend_ids") val friendIds: List<String>,
//    @ColumnInfo(name = "recent_place_ids") val recentPlaceIds: String,
)

@Entity
data class UserLocationUpdate(
    @PrimaryKey val userId: String = "",
    val lastLocationId: String = ""
)