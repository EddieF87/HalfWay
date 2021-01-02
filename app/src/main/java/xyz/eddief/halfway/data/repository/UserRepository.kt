package xyz.eddief.halfway.data.repository

import androidx.lifecycle.LiveData
import xyz.eddief.halfway.data.AppDatabase
import xyz.eddief.halfway.data.models.LocationObject
import xyz.eddief.halfway.data.models.User
import xyz.eddief.halfway.data.models.UserLocationUpdate
import xyz.eddief.halfway.data.models.UserWithLocations
import xyz.eddief.halfway.ui.main.home.LocationProfile
import javax.inject.Inject

class UserRepository @Inject constructor(private val appDatabase: AppDatabase) {

    private var userId = ""
    private val otherId1 get() = "${userId}_OTHER1"
    private val otherId2 get() = "${userId}_OTHER2"

    suspend fun updateUserLocation(location: LocationObject) = updateLocation(userId, location)

    suspend fun updateOtherLocation(userId: String, location: LocationObject) =
        updateLocation(userId, location)

    suspend fun updateLocationFromProfile(profile: LocationProfile, location: LocationObject) =
        updateLocation(
            when (profile) {
                LocationProfile.ME -> userId
                LocationProfile.OTHER_1 -> otherId1
                LocationProfile.OTHER_2 -> otherId2
            }, location
        )

    private suspend fun updateLocation(userId: String, location: LocationObject) {
        appDatabase.apply {
            locationDao().insert(location)
            userDao().updateLocation(UserLocationUpdate(userId, location.locationId))
        }
    }

    fun observeUserWithLocations(): LiveData<UserWithLocations?> =
        appDatabase.userDao().observeUserWithLocations(userId)

    fun observeOthersWithLocations(): LiveData<List<UserWithLocations>> =
        appDatabase.userDao().observeOthersWithLocations(otherId1, otherId2)

    suspend fun getUserWithLocations(userId: String): UserWithLocations =
        appDatabase.userDao().getUserWithLocations(userId)

    suspend fun updateUserId(id: String) {
        userId = id
        appDatabase.userDao().insertAll(
            User(userId, "testEd", "FOy", "email.com", ""),
            User(otherId1, "OTHER", "ID1", "email.com", ""),
            User(otherId2, "other", "ID2", "email.com", ""),
        )
    }
}