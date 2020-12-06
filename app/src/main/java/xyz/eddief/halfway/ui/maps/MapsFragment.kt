package xyz.eddief.halfway.ui.maps

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.fragment_maps.*
import xyz.eddief.halfway.R
import xyz.eddief.halfway.data.TestUtils

class MapsFragment : Fragment() {

    private val args: MapsFragmentArgs by navArgs()

    private val callback = OnMapReadyCallback { googleMap ->

        // Add a marker in Sydney and move the camera
        val location1 = TestUtils.TEST_LOC_1
        val location2 = TestUtils.TEST_LOC_2
        val center = LatLngBounds.builder().include(location1).include(location2).build().center

        val nearbyPlaces = args.place?.results?.map {
            Pair(it.geometry.location, it.name)
        }

//        map.setBuiltInZoomControls(true)

        googleMap.apply {

//            val clusterManager = ClusterManager<ClusterItem>(requireContext(), this)
//            clusterManager.setOnClusterClickListener(object : ClusterManager.OnClusterClickListener())

            nearbyPlaces?.forEach {
                addMarker(MarkerOptions().position(LatLng(it.first.lat, it.first.lng)).title(it.second))
            }

            addMarker(MarkerOptions().position(location1).title("Marker in Sydney"))
            addMarker(MarkerOptions().position(location2).title("Marker in NJ"))
            addMarker(MarkerOptions().position(center).title("Marker in center"))
            moveCamera(CameraUpdateFactory.newLatLng(location1))
            moveCamera(CameraUpdateFactory.newLatLngZoom(location1, 12f))



        }

        googleMap.setOnMarkerClickListener {
            Toast.makeText(requireContext(), it.title, Toast.LENGTH_SHORT).show()
            false
        }


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
    }
}