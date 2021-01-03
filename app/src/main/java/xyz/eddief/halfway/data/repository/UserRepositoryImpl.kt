package xyz.eddief.halfway.data.repository

import androidx.lifecycle.LiveData
import xyz.eddief.halfway.data.AppDatabase
import xyz.eddief.halfway.data.models.LocationObject
import xyz.eddief.halfway.data.models.User
import xyz.eddief.halfway.data.models.UserLocationUpdate
import xyz.eddief.halfway.data.models.UserWithLocations
import xyz.eddief.halfway.ui.main.home.LocationProfile
import javax.inject.Inject

//interface UserRepository {
//    suspend fun updateUserLocation(location: LocationObject)
//}


interface UserRepository {
    suspend fun updateUserLocation(location: LocationObject)
    suspend fun updateOtherLocation(userId: String, location: LocationObject)
    suspend fun updateLocationFromProfile(profile: LocationProfile, location: LocationObject)
    fun observeUserWithLocations(): LiveData<UserWithLocations?>
    fun observeOthersWithLocations(): LiveData<List<UserWithLocations>>
    suspend fun getUserWithLocations(userId: String): UserWithLocations
    suspend fun updateUserId(id: String)
}

class UserRepositoryImpl @Inject constructor(private val appDatabase: AppDatabase) :
    UserRepository {

    private var userId = ""
    private val otherId1 get() = "${userId}_OTHER1"
    private val otherId2 get() = "${userId}_OTHER2"

    override suspend fun updateUserLocation(location: LocationObject) =
        updateLocation(userId, location)

    override suspend fun updateOtherLocation(userId: String, location: LocationObject) =
        updateLocation(userId, location)

    override suspend fun updateLocationFromProfile(
        profile: LocationProfile,
        location: LocationObject
    ) =
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

    override fun observeUserWithLocations(): LiveData<UserWithLocations?> =
        appDatabase.userDao().observeUserWithLocations(userId)

    override fun observeOthersWithLocations(): LiveData<List<UserWithLocations>> =
        appDatabase.userDao().observeOthersWithLocations(otherId1, otherId2)

    override suspend fun getUserWithLocations(userId: String): UserWithLocations =
        appDatabase.userDao().getUserWithLocations(userId)

    override suspend fun updateUserId(id: String) {
        userId = id
        appDatabase.userDao().insertAll(
            User(userId, "testEd", "FOy", "email.com", ""),
            User(otherId1, "OTHER", "ID1", "email.com", ""),
            User(otherId2, "other", "ID2", "email.com", ""),
        )
    }
}