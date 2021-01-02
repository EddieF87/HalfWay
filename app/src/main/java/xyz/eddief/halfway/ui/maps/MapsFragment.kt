package xyz.eddief.halfway.ui.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.fragment_maps.*
import xyz.eddief.halfway.R
import xyz.eddief.halfway.data.models.MapData


class MapsFragment : Fragment() {

    private val argsMapData: MapData get() = arguments?.getParcelable(MAP_DATA_KEY)!!
    private var googleMap: GoogleMap? = null

    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap

        val nearbyPlaces = argsMapData.nearbyPlacesResult.results.map {
            Pair(it.geometry.location, it.name)
        }
        val myLocations = argsMapData.locations
        val centerLocation = argsMapData.centerLocation

        if (nearbyPlaces.isNullOrEmpty()) {
            this.view?.let {
                Snackbar.make(it, "No places found nearby :(", Snackbar.LENGTH_LONG).show()
            }
        }

        googleMap.apply {
            val clusterManager = ClusterManager<ClusterItem>(requireContext(), this)

            clusterManager.apply {
                setOnCameraIdleListener(this)
                markerCollection.setInfoWindowAdapter(object : InfoWindowAdapter {
                    override fun getInfoWindow(marker: Marker): View {
                        val inflater = LayoutInflater.from(requireContext())
                        val view: View = inflater.inflate(R.layout.custom_info_window, null)
                        val textView = view.findViewById<TextView>(R.id.textViewTitle)
                        val text = if (marker.title != null) marker.title else "Cluster Item"
                        textView.text = text
                        return view
                    }

                    override fun getInfoContents(marker: Marker): View? = null
                })
                markerCollection.setOnInfoWindowClickListener { marker: Marker? ->
                    Toast.makeText(
                        requireContext(),
                        "Info window clicked.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                setOnClusterClickListener {
                    animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            it.position,
                            cameraPosition.zoom + 4
                        )
                    )
                    true
                }
                setOnClusterItemClickListener {
                    Toast.makeText(requireContext(), it.title, Toast.LENGTH_SHORT).show()
                    false
                }

                addItems(nearbyPlaces.map { MyClusterItem(it.first.lat, it.first.lng, it.second ?: "") })
            }

            myLocations.forEach {
                addMarker(MarkerOptions().position(it.latLng).title(it.title))
                    .setIcon(
                        bitmapDescriptorFromVector(
                            requireContext(),
                            R.drawable.ic_notifications_black_24dp
                        )
                    )
            }
            addMarker(MarkerOptions().position(centerLocation.latLng).title(centerLocation.title))
                .setIcon(
                    bitmapDescriptorFromVector(
                        requireContext(),
                        R.drawable.ic_notifications_black_24dp
                    )
                )

            moveCamera(CameraUpdateFactory.newLatLngZoom(centerLocation.latLng, 10f))
        }
    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        @DrawableRes vectorDrawableResourceId: Int
    ): BitmapDescriptor? {
        val background = ContextCompat.getDrawable(
            context,
            R.drawable.common_google_signin_btn_icon_light_normal_background
        )
        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        vectorDrawable!!.setBounds(
            40,
            20,
            vectorDrawable.intrinsicWidth + 40,
            vectorDrawable.intrinsicHeight + 20
        )
        val bitmap = Bitmap.createBitmap(
            background.intrinsicWidth,
            background.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_maps, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        zoomOut.setOnClickListener {
            googleMap?.moveCamera(CameraUpdateFactory.zoomOut())
        }
    }

    companion object {
        private const val MAP_DATA_KEY = "my_boolean"

        fun newInstance(mapData: MapData?) = MapsFragment().apply {
            arguments = bundleOf(MAP_DATA_KEY to mapData)
        }
    }
}