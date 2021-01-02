package xyz.eddief.halfway

import com.google.gson.Gson
import xyz.eddief.halfway.data.models.DistanceResult
import xyz.eddief.halfway.data.models.GeoCode
import xyz.eddief.halfway.data.models.NearbyPlacesResult
import xyz.eddief.halfway.data.repository.MapsRepository

class FakeMapsRepository : MapsRepository {

    override suspend fun getNearbyPlaces(
        location: String,
        placeType: String,
        openNow: Boolean
    ): NearbyPlacesResult {
        return Gson().fromJson(placesJson1, NearbyPlacesResult::class.java)
    }

    override suspend fun getGeocode(latLng: String): GeoCode {
        return GeoCode(
            results = listOf(
                GeoCode.Result(
                    address_components = listOf(),
                    formatted_address = "",
                    geometry = GeoCode.Geometry(
                        location = GeoCode.Location(0.0, 0.0),
                        location_type = "",
                        viewport = GeoCode.Viewport(
                            GeoCode.Northeast(0.0, 0.0),
                            GeoCode.Southwest(0.0, 0.0)
                        )
                    ),
                    place_id = "",
                    types = listOf()
                )
            )
        )
    }

    override suspend fun getDistance(
        units: String,
        origins: String,
        destinations: String
    ): DistanceResult {
        return DistanceResult(
            destination_addresses = listOf(),
            origin_addresses = listOf(),
            error_message = "",
            rows = listOf(),
            status = ""
        )
    }
}