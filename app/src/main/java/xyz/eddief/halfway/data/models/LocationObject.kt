package xyz.eddief.halfway.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class LocationObject(
    @PrimaryKey val locationId: String = UUID.randomUUID().toString(),
    val title: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var address: String = ""
) : Parcelable {

//    constructor(
//        title: String = "",
//        latitude: Double,
//        longitude: Double,
//        address: String = ""
//    ) : this(UUID.randomUUID().toString(), title, latitude, longitude, address)

    fun latLng() = LatLng(latitude, longitude)
}