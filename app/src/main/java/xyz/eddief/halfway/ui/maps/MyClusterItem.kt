package xyz.eddief.halfway.ui.maps

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MyClusterItem(
    lat: Double,
    lng: Double,
    private val title: String = "",
    private val snippet: String = ""
) : ClusterItem {

    private val position: LatLng = LatLng(lat, lng)

    override fun getPosition(): LatLng = position

    override fun getTitle(): String = title

    override fun getSnippet(): String = snippet

}
