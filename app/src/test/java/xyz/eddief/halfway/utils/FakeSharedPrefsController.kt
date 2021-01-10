package xyz.eddief.halfway.utils

class FakeSharedPrefsController: SharedPreferencesController {
    override var placeToMeet: String = "test"
    override var isSearchByKeyword: Boolean = false
}