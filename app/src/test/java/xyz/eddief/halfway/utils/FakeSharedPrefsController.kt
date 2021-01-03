package xyz.eddief.halfway.utils

class FakeSharedPrefsController: SharedPreferencesController {
    override var placeToMeet: String
        get() = "test"
        set(value) {}
    override var isSearchByKeyword: Boolean
        get() = false
        set(value) {}
}