package xyz.eddief.halfway.utils

object PlaceTypeUtils {

    const val DEFAULT_KEY = "Anywhere"
    const val DEFAULT_VALUE = ""

    val placeTypesMap = mapOf(
        DEFAULT_KEY to DEFAULT_VALUE,
        "Restaurant" to "restaurant",
        "Cafe" to "cafe",
        "Park" to "park",
        "Mall" to "shopping_mall",
        "Movies" to "movie_theater",
        "Tourist Attraction" to "tourist_attraction",
        "Bar" to "bar",
        "Night Club" to "night_club"
    )

    fun hasPlaceKey(placeTypeKey: String) = placeTypesMap.containsKey(placeTypeKey)

    fun getPlaceKey(placeTypeValue: String): String =
        placeTypesMap.filterValues { it == placeTypeValue }.keys.firstOrNull() ?: DEFAULT_KEY

    fun getPlaceType(placeTypeKey: String) = placeTypesMap[placeTypeKey] ?: DEFAULT_VALUE
}