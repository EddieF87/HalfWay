package xyz.eddief.halfway.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import xyz.eddief.halfway.data.models.User
import xyz.eddief.halfway.data.models.UserLocationUpdate
import xyz.eddief.halfway.data.models.UserWithLocations

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE userId=(:userId)")
    suspend fun getUserWithLocations(userId: String): UserWithLocations

    @Query("SELECT * FROM user WHERE userId=(:userId)")
    fun observeUserWithLocations(userId: String): Flow<UserWithLocations?>

    @Query("SELECT * FROM user WHERE userId IN (:userIds)")
    fun observeOthersWithLocations(vararg userIds: String): Flow<List<UserWithLocations>>

    @Query("SELECT * FROM user WHERE userId IN (:userIds)")
    suspend fun loadAllByIds(userIds: Array<String>): List<User>

    @Query("SELECT * FROM user WHERE fullName LIKE :name LIMIT 1")
    suspend fun findByName(name: String): User

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg users: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceAll(vararg users: User)

    @Update(entity = User::class)
    suspend fun updateLocation(user: UserLocationUpdate)

    @Delete
    suspend fun delete(user: User)
}