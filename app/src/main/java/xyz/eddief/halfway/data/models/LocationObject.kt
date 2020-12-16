package xyz.eddief.halfway.data.models

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationObject(val title: String = "", val location: LatLng) : Parcelable