package com.matiasilveiro.automastichome.login.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.login.ui.navigatorstates.SplashNavigatorStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private val _navigation = SingleLiveEvent<SplashNavigatorStates>()
    val navigation: LiveData<SplashNavigatorStates>
        get() = _navigation

    fun goToSignIn (delayMillis: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            delay(delayMillis)
            _navigation.value = SplashNavigatorStates.ToSignIn
        }
    }

    fun goToMainApplication (delayMillis: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            delay(delayMillis)
            _navigation.value = SplashNavigatorStates.ToMainApplication
        }
    }
}