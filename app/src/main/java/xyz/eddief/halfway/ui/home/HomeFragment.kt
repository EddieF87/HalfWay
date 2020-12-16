package xyz.eddief.halfway.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import xyz.eddief.halfway.R
import xyz.eddief.halfway.data.TestUtils
import xyz.eddief.halfway.data.models.MapData
import xyz.eddief.halfway.data.models.NearbyPlacesResult

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.mapData.observe(viewLifecycleOwner, {
            it.get()?.let { places ->
                mapNearbyPlaces(places)
            }
        })

        val textView: TextView = view.findViewById(R.id.text_home)
        textView.setOnClickListener { homeViewModel.coordinate() }
    }

    private fun mapNearbyPlaces(mapData: MapData) {
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationMaps(mapData)
        findNavController().navigate(action)
    }
}