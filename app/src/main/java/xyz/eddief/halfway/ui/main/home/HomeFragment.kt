package xyz.eddief.halfway.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        val textView: TextView = view.findViewById(R.id.text_home)
        textView.setOnClickListener { homeViewModel.coordinate() }
    }

    private fun syncMapDataState(homeDataState: HomeDataState) {
        when(homeDataState) {
            is HomeDataState.Ready -> {
                displayLoading(false)
                mapNearbyPlaces(homeDataState.mapData)
            }
            is HomeDataState.Error -> {
                displayLoading(false)
                displayError(homeDataState.error)
            }
            HomeDataState.Loading -> displayLoading(true)
        }
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
        text_home.isVisible = !inProgress
    }
}