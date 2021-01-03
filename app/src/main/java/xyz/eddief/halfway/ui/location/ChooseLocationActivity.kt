package xyz.eddief.halfway.ui.location

import android.location.Geocoder
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import xyz.eddief.halfway.R
import xyz.eddief.halfway.data.models.LocationObject
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
            showLocationConfirmationDialog(location)
        }
    }

    private fun showLocationConfirmationDialog(location: LocationObject) = AlertDialog.Builder(this)
        .setTitle(R.string.choose_location_dialog_title)
        .setMessage(getString(R.string.choose_location_dialog_message, location.address))
        .setPositiveButton(R.string.dialog_positive) { _, _ ->
            chooseLocationViewModel.updateLocation(location)
            finish()
        }
        .setNegativeButton(R.string.dialog_negative) { _, _ ->  }
        .create()
        .show()

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