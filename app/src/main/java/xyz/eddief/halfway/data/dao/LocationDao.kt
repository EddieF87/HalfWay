package xyz.eddief.halfway.data.dao

import androidx.room.*
import xyz.eddief.halfway.data.models.LocationObject

@Dao interface LocationDao {

    @Query("SELECT * FROM locationObject")
    suspend fun getAll(): List<LocationObject>

    @Query("SELECT * FROM locationObject WHERE locationId IN (:locationIds)")
    suspend fun loadAllByIds(locationIds: Array<String>): List<LocationObject>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationObject: LocationObject) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg locationObjects: LocationObject)

    @Delete
    suspend fun delete(locationObject: LocationObject)

}