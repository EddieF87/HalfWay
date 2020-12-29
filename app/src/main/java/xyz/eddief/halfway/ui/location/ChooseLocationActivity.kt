package xyz.eddief.halfway.ui.location

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import xyz.eddief.halfway.R
import xyz.eddief.halfway.ui.main.home.HomeFragment

class ChooseLocationActivity : AppCompatActivity() {

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.setOnMapClickListener {
//            val intent = Intent()
//            intent.putExtra(HomeFragment.LOCATION_ADDRESS_KEY, it)
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(HomeFragment.LOCATION_ADDRESS_KEY, it)
            })
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_location)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}