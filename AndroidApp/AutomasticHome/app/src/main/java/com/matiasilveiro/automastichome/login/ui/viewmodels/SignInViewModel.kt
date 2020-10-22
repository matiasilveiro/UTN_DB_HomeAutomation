package com.matiasilveiro.automastichome.login.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.login.ui.navigatorstates.SignInNavigatorStates
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    private val _navigation = SingleLiveEvent<SignInNavigatorStates>()
    val navigation: LiveData<SignInNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState : LiveData<BaseViewState> get() = _viewState

    init {
        _viewState.value = BaseViewState.Ready
    }

    fun doLogin(email: String, password: String) {
        viewModelScope.launch {
            _viewState.value = BaseViewState.Loading

            /*
            when(val result = loginWithEmailAndPassword(email, password)) {
                is MyResult.Success -> {
                    _viewState.value = BaseViewState.Ready
                    goToMainApplication()
                }
                is MyResult.Failure -> {
                    _viewState.value = BaseViewState.Failure(result.exception)
                }
            }
             */
            delay(1000)
            _viewState.value = BaseViewState.Ready
            goToMainApplication()

        }
    }

    fun goToSignUp() {
        _navigation.value = SignInNavigatorStates.ToSignUp
    }

    fun goToMainApplication() {
        _navigation.value = SignInNavigatorStates.ToMainApplication
    }
}