package xyz.eddief.halfway.data.models

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.OpeningHours
import kotlinx.android.parcel.Parcelize


@Parcelize
data class NearbyPlacesResult(
    val html_attributions: List<HtmlAttribution>,
    val results: List<Result>,
    val status: String
) : Parcelable {


    @Parcelize
    data class Result(
        val geometry: Geometry,
        val icon: String,
        val id: String,
        val place_id: String,
        val name: String,
        val opening_hours: OpeningHours?,
        val reference: String,
        val types: List<String>,
        val vicinity: String
    ) : Parcelable


    @Parcelize
    data class Geometry(
        val location: Location,
        val viewport: Viewport
    ) : Parcelable


    @Parcelize
    data class Location(
        val lat: Double,
        val lng: Double
    ) : Parcelable


    @Parcelize
    data class Viewport(
        val northeast: Northeast,
        val southwest: Southwest
    ) : Parcelable


    @Parcelize
    data class Northeast(
        val lat: Double,
        val lng: Double
    ) : Parcelable


    @Parcelize
    data class Southwest(
        val lat: Double,
        val lng: Double
    ) : Parcelable


    @Parcelize
    data class HtmlAttribution(
        val attribution: String
    ) : Parcelable

    @Parcelize
    data class OpeningHours(
        val open_now: Boolean? = false
    ) : Parcelable
}