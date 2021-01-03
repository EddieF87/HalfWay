package xyz.eddief.halfway.data.repository

import retrofit2.await
import xyz.eddief.halfway.BuildConfig
import xyz.eddief.halfway.data.models.DistanceResult
import xyz.eddief.halfway.data.models.GeoCode
import xyz.eddief.halfway.data.models.NearbyPlacesResult
import xyz.eddief.halfway.data.service.MapsService
import javax.inject.Inject


interface MapsRepository {
    suspend fun getNearbyPlacesByType(
        location: String,
        placeToMeet: String,
        isKeyword: Boolean = false,
        openNow: Boolean
    ): NearbyPlacesResult
    suspend fun getGeocode(latLng: String): GeoCode?
    suspend fun getDistance(units: String, origins: String, destinations: String): DistanceResult?
}

class MapsRepositoryImpl @Inject constructor(private val mapsService: MapsService) :
    MapsRepository {

    override suspend fun getNearbyPlacesByType(
        location: String,
        placeToMeet: String,
        isKeyword: Boolean,
        openNow: Boolean
    ): NearbyPlacesResult =
        getNearbyPlacesRanked(
            location,
            placeToMeet,
            isKeyword,
            openNow
        ).takeIf { it.results.isNotEmpty() }
            ?: getNearbyPlacesInRadius(location, placeToMeet, isKeyword, MAPS_REQUEST_RADIUS)

    private suspend fun getNearbyPlacesRanked(
        location: String,
        placeToMeet: String,
        isKeyword: Boolean,
        openNow: Boolean
    ) = mapsService.getNearbyPlacesByKeyword(
        location = location,
        rankBy = MAPS_REQUEST_RANK_BY_DISTANCE,
        type = placeToMeet.takeIf { !isKeyword },
        keyword = placeToMeet.takeIf { isKeyword },
        openNow = openNow.toString(),
        key = MAPS_API_KEY
    ).await()

    private suspend fun getNearbyPlacesInRadius(
        location: String,
        placeToMeet: String,
        isKeyword: Boolean,
        radius: Int
    ) = mapsService.getNearbyPlacesByKeyword(
        location = location,
        radius = radius.toString(),
        openNow = false.toString(),
        type = placeToMeet.takeIf { !isKeyword },
        keyword = placeToMeet.takeIf { isKeyword },
        key = MAPS_API_KEY
    ).await()

    override suspend fun getGeocode(latLng: String): GeoCode? =
        mapsService.getGeocode(latLng, MAPS_API_KEY).await()

    override suspend fun getDistance(
        units: String,
        origins: String,
        destinations: String
    ): DistanceResult? = mapsService.getDistance(units, origins, destinations, MAPS_API_KEY).await()

    companion object {
        private const val MAPS_API_KEY = BuildConfig.MAPS_API_KEY
        private const val MAPS_REQUEST_RANK_BY_DISTANCE = "distance"
        private const val MAPS_REQUEST_RADIUS = 50_000
    }
}