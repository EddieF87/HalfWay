package xyz.eddief.halfway.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import xyz.eddief.halfway.data.models.LocationObject
import xyz.eddief.halfway.data.models.User
import xyz.eddief.halfway.data.models.UserWithLocations
import xyz.eddief.halfway.ui.main.home.LocationProfile

class FakeUserRepository : UserRepository {
    override suspend fun updateUserLocation(location: LocationObject) {

    }

    override suspend fun updateOtherLocation(userId: String, location: LocationObject) {

    }

    override suspend fun updateLocationFromProfile(
        profile: LocationProfile,
        location: LocationObject
    ) {

    }

    override fun observeUserWithLocations(): LiveData<UserWithLocations?> {
        return MutableLiveData(null)
    }

    override fun observeOthersWithLocations(): LiveData<List<UserWithLocations>> {
        return MutableLiveData(emptyList<UserWithLocations>())
    }

    override suspend fun getUserWithLocations(userId: String): UserWithLocations {
        return UserWithLocations(User(userId, "testEd", "FOy", "email.com", ""))
    }

    override suspend fun updateUserId(id: String) {

    }
}