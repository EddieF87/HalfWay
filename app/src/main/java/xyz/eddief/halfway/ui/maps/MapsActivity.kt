package xyz.eddief.halfway.ui.maps

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import xyz.eddief.halfway.R
import xyz.eddief.halfway.data.models.MapData
import xyz.eddief.halfway.ui.main.home.HomeFragment.Companion.MAPS_DATA_KEY

@AndroidEntryPoint
class MapsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapData = intent.extras?.getParcelable<MapData?>(MAPS_DATA_KEY)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mapsFragmentContainer, MapsFragment.newInstance(mapData))
            .commit()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}