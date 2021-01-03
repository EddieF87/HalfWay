package xyz.eddief.halfway.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

interface SharedPreferencesController {
    var placeToMeet: String
    var isSearchByKeyword: Boolean
}

class SharedPreferencesControllerImpl @Inject constructor(context: Context): SharedPreferencesController {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        context.packageName,
        Context.MODE_PRIVATE
    )
    override var placeToMeet: String
        get() = sharedPreferences.getString(PLACE_TO_MEET, "") ?: ""
        set(value) = sharedPreferences.edit().putString(PLACE_TO_MEET, value).apply()
    override var isSearchByKeyword: Boolean
        get() = sharedPreferences.getBoolean(IS_SEARCH_BY_KEYWORD, false)
        set(value) = sharedPreferences.edit().putBoolean(IS_SEARCH_BY_KEYWORD, value).apply()

    companion object {
        const val PLACE_TO_MEET = "placeToMeet"
        const val IS_SEARCH_BY_KEYWORD = "isSearchByKeyword"
    }
}