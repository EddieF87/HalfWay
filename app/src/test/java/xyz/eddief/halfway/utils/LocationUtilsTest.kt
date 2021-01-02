package xyz.eddief.halfway.utils

import android.location.Address
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.*

class LocationUtilsTest {
    
    
    @Test
    fun getAddressFromGeoCodeResults_noResults_returnsValidString() {
        val value = LocationUtils.getAddressFromGeoCodeResults(emptyList())
        assertNotNull(value)
    }

    @Test
    fun getAddressFromGeoCodeResults_emptyAddress_returnsValidString() {
        val value = LocationUtils.getAddressFromGeoCodeResults(
            listOf(Address(Locale.getDefault()))
        )
        assertNotNull(value)
    }
}