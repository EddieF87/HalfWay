package xyz.eddief.halfway.ui.main.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import xyz.eddief.halfway.data.models.LocationObject
import xyz.eddief.halfway.data.repository.FakeMapsRepository
import xyz.eddief.halfway.data.repository.FakeUserRepository
import xyz.eddief.halfway.data.repository.MapsRepository
import xyz.eddief.halfway.data.repository.UserRepository
import xyz.eddief.halfway.utils.FakeSharedPrefsController
import xyz.eddief.halfway.utils.PlaceTypeUtils
import xyz.eddief.halfway.utils.SharedPreferencesController

class HomeViewModelTest {

    private lateinit var userRepository: UserRepository
    private lateinit var mapsRepository: MapsRepository
    private lateinit var sharedPrefsController: SharedPreferencesController
    private lateinit var homeViewModel: HomeViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

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

    @Test
    fun updatePlaceType_WithKeyword_DisplaysCorrect() {
        val placeType = "HungryJacks"
        homeViewModel.updatePlaceType(placeType, true)
        assertEquals(placeType, homeViewModel.placeToMeetDisplay.value)
        assertEquals(placeType, sharedPrefsController.placeToMeet)

        val placeType2 = "Night Club"
        homeViewModel.updatePlaceType(placeType2, true)
        assertEquals(placeType2, homeViewModel.placeToMeetDisplay.value)
        assertEquals(placeType2, sharedPrefsController.placeToMeet)


        homeViewModel.updatePlaceType(null, true)
        assertEquals(placeType2, homeViewModel.placeToMeetDisplay.value)
        assertEquals(placeType2, sharedPrefsController.placeToMeet)
    }

    @Test
    fun updatePlaceType_WithoutKeyword_DisplaysCorrect() {
        val placeType = "McDonalds"
        homeViewModel.updatePlaceType(placeType, true)
        assertEquals(placeType, homeViewModel.placeToMeetDisplay.value)
        assertEquals(placeType, sharedPrefsController.placeToMeet)

        val placeTypeKey = "Night Club"
        val placeTypeValue = "night_club"
        homeViewModel.updatePlaceType(placeTypeKey, false)
        assertEquals(placeTypeKey, homeViewModel.placeToMeetDisplay.value)
        assertEquals(placeTypeValue, sharedPrefsController.placeToMeet)

        homeViewModel.updatePlaceType(null, false)
        assertEquals(placeTypeKey, homeViewModel.placeToMeetDisplay.value)
        assertEquals(placeTypeValue, sharedPrefsController.placeToMeet)

        val gibberish = "evwcdss"
        homeViewModel.updatePlaceType(gibberish, false)
        assertEquals(PlaceTypeUtils.DEFAULT_KEY, homeViewModel.placeToMeetDisplay.value)
        assertEquals(PlaceTypeUtils.DEFAULT_VALUE, sharedPrefsController.placeToMeet)
    }
}