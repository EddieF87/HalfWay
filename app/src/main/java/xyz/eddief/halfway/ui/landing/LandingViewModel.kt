package xyz.eddief.halfway.ui.landing

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.eddief.halfway.data.repository.UserRepository
import xyz.eddief.halfway.utils.dLog

class LandingViewModel @ViewModelInject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _done = MutableLiveData(false)
    val done: LiveData<Boolean>
        get() = _done

    fun setUserId(id: String) {
        dLog("sssssss setUserId = $id")
        viewModelScope.launch(Dispatchers.Main) {
            userRepository.updateUserId(id)
            _done.value = true
        }
    }

}