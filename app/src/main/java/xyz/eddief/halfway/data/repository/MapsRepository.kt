package xyz.eddief.halfway.data.repository

import retrofit2.await
import xyz.eddief.halfway.BuildConfig
import xyz.eddief.halfway.data.models.DistanceResult
import xyz.eddief.halfway.data.models.GeoCode
import xyz.eddief.halfway.data.models.NearbyPlacesResult
import xyz.eddief.halfway.data.service.MapsService
import javax.inject.Inject


interface MapsRepository {
    suspend fun getNearbyPlaces(
        location: String,
        placeType: String,
        openNow: Boolean
    ): NearbyPlacesResult

    suspend fun getGeocode(latLng: String): GeoCode?
    suspend fun getDistance(units: String, origins: String, destinations: String): DistanceResult?
}

class MapsRepositoryImpl @Inject constructor(private val mapsService: MapsService) :
    MapsRepository {

    override suspend fun getNearbyPlaces(
        location: String,
        placeType: String,
        openNow: Boolean
    ): NearbyPlacesResult =
        mapsService.getNearbyPlaces(
            location = location,
            rankBy = "distance",
            type = placeType,
            openNow = openNow.toString(),
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
    }
}