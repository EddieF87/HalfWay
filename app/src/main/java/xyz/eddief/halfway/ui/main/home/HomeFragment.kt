package xyz.eddief.halfway.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import xyz.eddief.halfway.R
import xyz.eddief.halfway.data.models.MapData
import xyz.eddief.halfway.ui.maps.MapsActivity

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

        homeViewModel.homeDataState.observe(viewLifecycleOwner, {
            it.get()?.let { state ->
                syncMapDataState(state)
            }
        })

        homeViewModel.placeType.observe(viewLifecycleOwner, {
            syncPlaceType(it)
        })

        homeSubmit.setOnClickListener { homeViewModel.coordinate() }
        homePlacesType.setOnClickListener { createDialog() }
        homeOpenNowCheckBox.setOnCheckedChangeListener { _, isChecked ->
            homeViewModel.openNowChecked = isChecked
        }
    }

    override fun onResume() {
        super.onResume()
        homeProfileMe.setProfile(true, "Address Text\nAddress Text\nAddress Text")
        homeProfileOther1.setProfile(false, "Enter Address")
        homeProfileOther2.setProfile(false, "Enter Address")
    }

    override fun onStop() {
        super.onStop()
        displayLoading(false)
    }

    private fun syncMapDataState(homeDataState: HomeDataState) = when (homeDataState) {
        is HomeDataState.Ready -> {
            mapNearbyPlaces(homeDataState.mapData)
        }
        is HomeDataState.Error -> {
            displayLoading(false)
            displayError(homeDataState.error)
        }
        HomeDataState.Loading -> displayLoading(true)
    }

    private fun mapNearbyPlaces(mapData: MapData) {
        val mapsIntent = Intent(requireActivity(), MapsActivity::class.java)
        val b = Bundle()
        b.putParcelable("mapData", mapData)
        mapsIntent.putExtras(b)
        startActivity(mapsIntent)
    }

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


    private fun createDialog() {
        AlertDialog.Builder(requireActivity())
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
    }
}