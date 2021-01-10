package xyz.eddief.halfway.ui.main.home

import android.Manifest
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
import xyz.eddief.halfway.data.models.UserWithLocations
import xyz.eddief.halfway.ui.location.ChooseLocationActivity
import xyz.eddief.halfway.ui.maps.MapsActivity
import xyz.eddief.halfway.utils.LocationUtils
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment(), PlaceToMeetDialogListener {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.apply {
            allLocationProfiles.observe(viewLifecycleOwner, { syncUsersWithLocations(it) })
            placeToMeetDisplay.observe(viewLifecycleOwner, { syncPlaceToMeet(it) })
            centerLatLng.observe(viewLifecycleOwner, { syncCenterLocation(it) })
            homeDataState.observe(
                viewLifecycleOwner,
                { it.get()?.let { state -> syncMapDataState(state) } })
        }

        homeSubmit.setOnClickListener { homeViewModel.fetchNearbyPlaces() }
        homePlacesType.setOnClickListener { showCreatePlaceTypesDialog() }
        homeProfileMe.setOnClickListener { showUpdateLocationDialog() }
        homeProfileOther1.setOnClickListener { goToChooseLocation(LocationProfile.OTHER_1) }
        homeProfileOther2.setOnClickListener { goToChooseLocation(LocationProfile.OTHER_2) }
        homeOpenNowCheckBox.setOnCheckedChangeListener { _, isChecked ->
            homeViewModel.openNowChecked = isChecked
        }
    }

    override fun onStop() {
        super.onStop()
        displayLoading(false)
    }

    private fun updateLocation(profile: LocationProfile, latLng: LatLng) =
        homeViewModel.updateLocation(
            profile,
            LocationUtils.createLocationObjectFromLatLng(
                Geocoder(requireContext(), Locale.getDefault()), latLng
            )
        )

    private fun syncMapDataState(state: HomeDataState) = when (state) {
        is HomeDataState.Ready -> goToMapNearbyPlaces(state.mapData)
        is HomeDataState.Error -> {
            displayLoading(false)
            displayError(state.error)
        }
        HomeDataState.Loading -> displayLoading(true)
    }

    private fun syncUsersWithLocations(usersPair: Pair<UserWithLocations?, List<UserWithLocations>>) {
        val userProfile: UserWithLocations? = usersPair.first
        if (userProfile == null) {
            //TODO GO TO LOGIN/ACCOUNT SET UP
            displayError("TODO: userProfile == null")
            return
        }
        val otherProfiles: List<UserWithLocations> = usersPair.second
        val otherProfile1 = otherProfiles.getOrNull(0)
        val otherProfile2 = otherProfiles.getOrNull(1)
        val userHasLocation = userProfile.hasLocation

        homeProfileMe.setProfile(userProfile)
        homeProfileOther1.apply {
            isVisible = otherProfile1 != null || userHasLocation
            setProfile(otherProfile1)
        }
        homeProfileOther2.apply {
            isVisible = otherProfile2 != null || otherProfile1?.hasLocation == true
            setProfile(otherProfile2)
        }

        homeSubmit.isEnabled = userHasLocation && otherProfiles.any { it.hasLocation }

        homeProfileLineMe.setLine(userProfile)
        homeProfileLineOther1.setLine(otherProfile1)
        homeProfileLineOther2.setLine(otherProfile2)
    }

    private fun syncPlaceToMeet(type: String) {
        homePlacesType.text = type
    }

    private fun syncCenterLocation(latLng: LatLng) {
        with(
            LocationUtils.createLocationObjectFromLatLng(
                Geocoder(requireContext(), Locale.getDefault()), latLng
            )
        ) {
            homeViewModel.centerLocation = this
            homeCenterAddress.text = this.address
        }
    }

    private fun displayError(error: String?) {
        Snackbar.make(requireView(), "$error", Snackbar.LENGTH_LONG).show()
    }

    private fun displayLoading(inProgress: Boolean) {
        homeLoader.isVisible = inProgress
        homeContent.isVisible = !inProgress
    }

    private fun goToMapNearbyPlaces(mapData: MapData) {
        val mapsIntent = Intent(requireActivity(), MapsActivity::class.java)
        mapsIntent.putExtras(Bundle().apply {
            putParcelable(MAPS_DATA_KEY, mapData)
        })
        startActivity(mapsIntent)
    }

    private fun goToChooseLocation(profile: LocationProfile) = startActivity(
        Intent(requireActivity(), ChooseLocationActivity::class.java).apply {
            putExtra(ChooseLocationActivity.PROFILE_KEY, profile)
        }
    )

    private fun showCreatePlaceTypesDialog() {
        val dialog = HomePlaceToMeetDialog()
        dialog.setTargetFragment(this, 49)
        dialog.show(parentFragmentManager, "placeToMeet")
    }

    private fun showUpdateLocationDialog() = AlertDialog.Builder(requireActivity())
        .setTitle(R.string.home_update_dialog_title)
        .setPositiveButton(R.string.home_update_dialog_positive) { _, _ ->
            goToChooseLocation(
                LocationProfile.ME
            )
        }
        .setNegativeButton(R.string.home_update_dialog_negative) { _, _ -> getUserLocation() }
        .setNeutralButton(R.string.dialog_cancel) { _, _ -> }
        .create()
        .show()

    private fun getUserLocation() {
        val lm =
            requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.let {
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
                if ((grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED)) {
                    getUserLocation()
                } else {
                    displayError(getString(R.string.location_permission_denied_error))
                }
            }
        }
    }

    override fun onPlaceTypeChosen(placeType: String) =
        homeViewModel.updatePlaceType(placeType, isKeyword = false)

    override fun onPlaceToMeetEntered(placeToMeet: String) =
        homeViewModel.updatePlaceType(placeToMeet, isKeyword = true)

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 429
        const val MAPS_DATA_KEY = "mapData"
    }
}