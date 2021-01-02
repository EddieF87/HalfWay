package xyz.eddief.halfway.ui.location

import android.location.Geocoder
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import xyz.eddief.halfway.R
import xyz.eddief.halfway.ui.main.home.LocationProfile
import xyz.eddief.halfway.utils.LocationUtils

@AndroidEntryPoint
class ChooseLocationActivity : AppCompatActivity() {

    private val chooseLocationViewModel: ChooseLocationViewModel by viewModels()

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.setOnMapClickListener {
            val location = LocationUtils.createLocationObjectFromLatLng(Geocoder(this), it)
            chooseLocationViewModel.updateLocation(location)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_location)
        (intent.extras?.get(PROFILE_KEY) as? LocationProfile)?.let {
            chooseLocationViewModel.profile = it
            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        } ?: finish()
    }

    companion object {
        const val PROFILE_KEY = "profile"
    }
}