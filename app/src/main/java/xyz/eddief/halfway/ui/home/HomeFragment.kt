package xyz.eddief.halfway.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import xyz.eddief.halfway.R
import xyz.eddief.halfway.data.models.MapData

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