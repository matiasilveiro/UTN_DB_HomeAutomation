package com.matiasilveiro.automastichome.login.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manadigital.tecontactolocal.CoreFeature.usecases.SendPasswordResetEmailUseCase
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.usecases.LoginWithEmailAndPasswordUseCase
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.login.ui.navigatorstates.SignInNavigatorStates
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInViewModel @ViewModelInject constructor(
    val loginWithEmailAndPassword: LoginWithEmailAndPasswordUseCase,
    val sendPasswordResetEmail: SendPasswordResetEmailUseCase
) : ViewModel() {
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

            when(val result = loginWithEmailAndPassword(email, password)) {
                is MyResult.Success -> {
                    _viewState.value = BaseViewState.Ready
                    goToMainApplication()
                }
                is MyResult.Failure -> {
                    _viewState.value = BaseViewState.Failure(result.exception)
                }
            }
        }
    }

    fun goToSignUp() {
        _navigation.value = SignInNavigatorStates.ToSignUp
    }

    fun goToMainApplication() {
        _navigation.value = SignInNavigatorStates.ToMainApplication
    }
}