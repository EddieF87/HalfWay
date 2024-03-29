package xyz.eddief.halfway.data.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import xyz.eddief.halfway.data.models.DistanceResult
import xyz.eddief.halfway.data.models.GeoCode
import xyz.eddief.halfway.data.models.NearbyPlacesResult


interface MapsService {

    @GET("distancematrix/json")
    fun getDistance(
        @Query("units") units: String,
        @Query("origins") origins: String,
        @Query("destinations") destinations: String,
        @Query("key") key: String
    ): Call<DistanceResult?>

    @GET("geocode/json")
    fun getGeocode(@Query("latlng") latLng: String, @Query("key") key: String): Call<GeoCode?>

//    @GET("place/nearbysearch/json")
//    fun getNearbyPlacesRanked(
//        @Query("location") location: String,
//        @Query("rankby") rankBy: String,
//        @Query("type") type: String,
//        @Query("open_now") openNow: String,
//        @Query("key") key: String
//    ): Call<NearbyPlacesResult>
//
//    @GET("place/nearbysearch/json")
//    fun getNearbyPlacesInRadius(
//        @Query("location") location: String,
//        @Query("radius") radius: String,
//        @Query("type") type: String,
//        @Query("key") key: String
//    ): Call<NearbyPlacesResult>

    @GET("place/nearbysearch/json")
    fun getNearbyPlacesByKeyword(
        @Query("location") location: String,
        @Query("radius") radius: String? = null,
        @Query("rankby") rankBy: String? = null,
        @Query("keyword") keyword: String? = null,
        @Query("type") type: String? = null,
        @Query("open_now") openNow: String,
        @Query("key") key: String
    ): Call<NearbyPlacesResult>

}