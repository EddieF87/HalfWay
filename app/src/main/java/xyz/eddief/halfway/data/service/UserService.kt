package xyz.eddief.halfway.data.service

import retrofit2.await
import xyz.eddief.halfway.BuildConfig
import xyz.eddief.halfway.data.models.DistanceResult
import xyz.eddief.halfway.data.models.GeoCode
import xyz.eddief.halfway.data.models.NearbyPlacesResult

class UserService(networkService: NetworkService) {

    private val mapsService: MapsService = networkService.mapsService

    suspend fun getNearbyPlaces(location: String, radius: String): NearbyPlacesResult? =
        mapsService.getNearbyPlaces(location, radius, MAPS_API_KEY).await()

    suspend fun getGeocode(latLng: String): GeoCode? =
        mapsService.getGeocode(latLng, MAPS_API_KEY).await()

    suspend fun getDistance(units: String, origins: String, destinations: String): DistanceResult? =
        mapsService.getDistance(units, origins, destinations, MAPS_API_KEY).await()

    companion object {
        private const val MAPS_API_KEY = BuildConfig.MAPS_API_KEY
    }
}