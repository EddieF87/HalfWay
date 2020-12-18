package xyz.eddief.halfway.data.service

import retrofit2.await
import xyz.eddief.halfway.BuildConfig
import xyz.eddief.halfway.data.models.DistanceResult
import xyz.eddief.halfway.data.models.GeoCode
import xyz.eddief.halfway.data.models.NearbyPlacesResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserService @Inject constructor(private val networkService: NetworkService) {

    suspend fun getNearbyPlaces(location: String, radius: String): NearbyPlacesResult? =
        networkService.mapsService.getNearbyPlaces(location, "distance", "restaurant", MAPS_API_KEY, true.toString()).await()

    suspend fun getGeocode(latLng: String): GeoCode? =
        networkService.mapsService.getGeocode(latLng, MAPS_API_KEY).await()

    suspend fun getDistance(units: String, origins: String, destinations: String): DistanceResult? =
        networkService.mapsService.getDistance(units, origins, destinations, MAPS_API_KEY).await()

    companion object {
        private const val MAPS_API_KEY = BuildConfig.MAPS_API_KEY
    }
}