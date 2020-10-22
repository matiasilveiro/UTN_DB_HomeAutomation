package com.matiasilveiro.automastichome.login.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.login.ui.navigatorstates.SignUpNavigatorStates

class SignUpViewModel : ViewModel() {

    private val _navigation = SingleLiveEvent<SignUpNavigatorStates>()
    val navigation: LiveData<SignUpNavigatorStates> get() = _navigation

    fun goBack() {
        _navigation.value = SignUpNavigatorStates.GoBack
    }
}