package xyz.eddief.halfway.ui.main.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import xyz.eddief.halfway.R
import xyz.eddief.halfway.data.models.MapData
import xyz.eddief.halfway.ui.location.ChooseLocationActivity
import xyz.eddief.halfway.ui.maps.MapsActivity
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.homeDataState.observe(
            viewLifecycleOwner,
            { it.get()?.let { state -> syncMapDataState(state) } })

        homeViewModel.placeType.observe(viewLifecycleOwner, { syncPlaceType(it) })
        homeViewModel.locationsAmount.observe(viewLifecycleOwner, { syncProfileStates(it) })

        homeSubmit.setOnClickListener { homeViewModel.coordinate() }
        homePlacesType.setOnClickListener { createPlaceTypesDialog() }
        homeProfileMe.setOnClickListener { goToChooseLocation(LOCATION_REQUEST_KEY_PROFILE_ME) }
        homeProfileOther1.setOnClickListener { goToChooseLocation(LOCATION_REQUEST_KEY_PROFILE_1) }
        homeProfileOther2.setOnClickListener { goToChooseLocation(LOCATION_REQUEST_KEY_PROFILE_2) }
        homeOpenNowCheckBox.setOnCheckedChangeListener { _, isChecked ->
            homeViewModel.openNowChecked = isChecked
        }

        getUserLocation()
    }

    override fun onStop() {
        super.onStop()
        displayLoading(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                LOCATION_REQUEST_KEY_PROFILE_ME -> updateLocation(LocationProfile.ME, data)
                LOCATION_REQUEST_KEY_PROFILE_1 -> updateLocation(LocationProfile.OTHER_1, data)
                LOCATION_REQUEST_KEY_PROFILE_2 -> updateLocation(LocationProfile.OTHER_2, data)
            }
        }
    }

    private fun updateLocation(profile: LocationProfile, data: Intent?) {
        (data?.extras?.get(LOCATION_ADDRESS_KEY) as? LatLng)?.let { updateLocation(profile, it) }
    }

    private fun updateLocation(profile: LocationProfile, latLng: LatLng) {
        homeViewModel.updateLocations(
            Geocoder(requireContext(), Locale.getDefault()),
            latLng,
            profile
        )
    }

    private fun updateProfileAddress(profile: LocationProfile, address: String) = when (profile) {
        LocationProfile.ME -> homeProfileMe
        LocationProfile.OTHER_1 -> homeProfileOther1
        LocationProfile.OTHER_2 -> homeProfileOther2
    }.setProfile(true, address)

    private fun syncMapDataState(state: HomeDataState) = when (state) {
        is HomeDataState.Ready -> goToMapNearbyPlaces(state.mapData)
        is HomeDataState.UpdateLocation -> updateProfileAddress(state.profile, state.address)
        is HomeDataState.Error -> {
            displayLoading(false)
            displayError(state.error)
        }
        HomeDataState.Loading -> displayLoading(true)
    }

    private fun syncProfileStates(locationsAmount: Int) {
        homeSubmit.isEnabled = locationsAmount > 1
        homeProfileMe.test(locationsAmount, -1)
        homeProfileOther1.test(locationsAmount, 0)
        homeProfileOther2.test(locationsAmount, 1)
    }

    private fun goToMapNearbyPlaces(mapData: MapData) {
        val mapsIntent = Intent(requireActivity(), MapsActivity::class.java)
        mapsIntent.putExtras(Bundle().apply {
            putParcelable(MAPS_DATA_KEY, mapData)
        })
        startActivity(mapsIntent)
    }

    private fun goToChooseLocation(requestCode: Int) = startActivityForResult(
        Intent(requireActivity(), ChooseLocationActivity::class.java),
        requestCode
    )

    private fun displayError(error: String?) {
        Snackbar.make(requireView(), "Error: $error", Snackbar.LENGTH_LONG).show()
    }

    private fun displayLoading(inProgress: Boolean) {
        homeLoader.isVisible = inProgress
        homeContent.isVisible = !inProgress
    }

    private fun syncPlaceType(type: String) {
        val index = resources.getStringArray(R.array.place_types_values).indexOf(type)
        homePlacesType.text = resources.getStringArray(R.array.place_types_labels)[index]
    }

    private fun createPlaceTypesDialog() = AlertDialog.Builder(requireActivity())
        .setTitle(R.string.home_place_type_label)
        .setItems(
            R.array.place_types_labels
        ) { _, index ->
            homeViewModel.updatePlaceType(
                resources.getStringArray(R.array.place_types_values)[index]
            )
        }
        .create()
        .show()

    private fun getUserLocation() {
        val lm = activity?.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            updateLocation(LocationProfile.ME, LatLng(it.latitude, it.longitude))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    getUserLocation()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    companion object {
        const val LOCATION_REQUEST_KEY_PROFILE_ME = 300
        const val LOCATION_REQUEST_KEY_PROFILE_1 = 301
        const val LOCATION_REQUEST_KEY_PROFILE_2 = 302
        const val LOCATION_ADDRESS_KEY = "address"
        const val LOCATION_PERMISSION_REQUEST_CODE = 429
        const val MAPS_DATA_KEY = "mapData"
    }
}