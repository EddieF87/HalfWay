package xyz.eddief.halfway.data.repository

import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.eddief.halfway.data.AppDatabase
import xyz.eddief.halfway.data.models.LocationObject
import xyz.eddief.halfway.data.models.User
import xyz.eddief.halfway.data.models.UserLocationUpdate
import xyz.eddief.halfway.data.models.UserWithLocations
import xyz.eddief.halfway.data.service.FirestoreService
import xyz.eddief.halfway.ui.main.home.LocationProfile
import xyz.eddief.halfway.utils.dLog
import javax.inject.Inject

interface UserRepository {
    val allSelectedWithLocationsFlow: Flow<Pair<UserWithLocations?, List<UserWithLocations>>>
    suspend fun updateUserLocation(location: LocationObject)
    suspend fun updateOtherLocation(userId: String, location: LocationObject)
    suspend fun updateLocationFromProfile(profile: LocationProfile, location: LocationObject)
    fun observeUserWithLocations(): Flow<UserWithLocations?>
    fun observeOthersWithLocations(): Flow<List<UserWithLocations>>
    suspend fun getUserWithLocations(userId: String): UserWithLocations
    suspend fun updateUserId(id: String)
}

class UserRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val firestoreService: FirestoreService
) : UserRepository {

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

    override val allSelectedWithLocationsFlow: Flow<Pair<UserWithLocations?, List<UserWithLocations>>>
        get() = observeUserWithLocations()
            .combine(observeOthersWithLocations()) { myProfile, otherProfiles ->
                Pair(myProfile, otherProfiles)
            }
            .flowOn(Dispatchers.IO)


    override fun observeUserWithLocations(): Flow<UserWithLocations?> {
        GlobalScope.launch {
            val t = appDatabase.userDao().getAll()
            dLog("uuuuuuu getall = $t")
        }
        val i = appDatabase.userDao().observeUserWithLocations(userId)
        dLog("uuuuuuuu observeUserWithLocations ${i.asLiveData().value}")
        return i
    }

    override fun observeOthersWithLocations(): Flow<List<UserWithLocations>> {
        GlobalScope.launch {
            val t = appDatabase.userDao().getAll()
            dLog("uuuuuuu getall = $t")
        }
        val i = appDatabase.userDao().observeOthersWithLocations(otherId1, otherId2)
        dLog("uuuuuuuu observeUserWithLocations $otherId1 $otherId2 ${i.asLiveData().value}")
        return i
    }

    override suspend fun getUserWithLocations(userId: String): UserWithLocations =
        appDatabase.userDao().getUserWithLocations(userId)

    override suspend fun updateUserId(id: String): Unit = withContext(Dispatchers.IO) {
        userId = id

        val user = firestoreService.fetchUser(userId)
        val currentLocation = user?.currentLocation
        dLog("uuuuuuuu uusuuuuer  $user")
        appDatabase.userDao().insertAll(
            User(userId, "testEd", "email.com", ""),
            User(otherId1, "OTHER", "email.com", ""),
            User(otherId2, "other", "email.com", ""),
        )

        currentLocation?.let {
            appDatabase.locationDao().insert(currentLocation)
        }
        user?.user?.let {
            appDatabase.userDao().insertAll(it)
        }

//        firestoreService.updateUser(appDatabase.userDao().getUserWithLocations(id))
    }
}