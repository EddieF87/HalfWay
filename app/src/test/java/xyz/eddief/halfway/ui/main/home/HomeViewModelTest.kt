package xyz.eddief.halfway.ui.main.home

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import xyz.eddief.halfway.data.models.LocationObject
import xyz.eddief.halfway.data.repository.FakeMapsRepository
import xyz.eddief.halfway.data.repository.FakeUserRepository
import xyz.eddief.halfway.data.repository.MapsRepository
import xyz.eddief.halfway.data.repository.UserRepository
import xyz.eddief.halfway.utils.FakeSharedPrefsController
import xyz.eddief.halfway.utils.SharedPreferencesController

class HomeViewModelTest {

    private lateinit var userRepository: UserRepository
    private lateinit var mapsRepository: MapsRepository
    private lateinit var sharedPrefsController: SharedPreferencesController
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setupViewModel() {
        sharedPrefsController = FakeSharedPrefsController()
        userRepository = FakeUserRepository()
        mapsRepository = FakeMapsRepository()
        homeViewModel =
            HomeViewModel(mapsRepository, userRepository, sharedPrefsController, SavedStateHandle())
    }

    @Test
    fun getCenterLocation_WithEmptyList_ReturnsNull() {
        val emptyListOfLocations = emptyList<LocationObject>()
        assertNull(homeViewModel.getCenterLocation(emptyListOfLocations))
    }

    @Test
    fun getCenterLocation_WithOneLocation_DoesNotCrash() {
        val listOfOneLocation =
            listOf(LocationObject(locationId = "", latitude = 0.0, longitude = 0.0))
        homeViewModel.getCenterLocation(listOfOneLocation)
        assertNull(homeViewModel.getCenterLocation(listOfOneLocation))
    }
}