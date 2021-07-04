package xyz.eddief.halfway.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import xyz.eddief.halfway.data.models.LocationObject
import xyz.eddief.halfway.data.models.User
import xyz.eddief.halfway.data.models.UserWithLocations
import xyz.eddief.halfway.ui.main.home.LocationProfile

class FakeUserRepository : UserRepository {
    override val allSelectedWithLocationsFlow: Flow<Pair<UserWithLocations?, List<UserWithLocations>>>
        get() = flow {
            emit(Pair(getUserWithLocations(""), listOf(getUserWithLocations(""))))
        }

    override suspend fun updateUserLocation(location: LocationObject) {

    }

    override suspend fun updateOtherLocation(userId: String, location: LocationObject) {

    }

    override suspend fun updateLocationFromProfile(
        profile: LocationProfile,
        location: LocationObject
    ) {

    }

    override fun observeUserWithLocations(): Flow<UserWithLocations?> {
        return flowOf(null)
    }

    override fun observeOthersWithLocations(): Flow<List<UserWithLocations>> {
        return flowOf(emptyList<UserWithLocations>())
    }

    override suspend fun getUserWithLocations(userId: String): UserWithLocations {
        return UserWithLocations(User(userId, "testEd", "FOy", "email.com"))
    }

    override suspend fun updateUserId(id: String) {

    }
}