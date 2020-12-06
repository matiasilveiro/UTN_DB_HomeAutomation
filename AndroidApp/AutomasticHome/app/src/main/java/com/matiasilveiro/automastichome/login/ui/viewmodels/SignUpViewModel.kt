package com.matiasilveiro.automastichome.login.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manadigital.tecontactolocal.CoreFeature.usecases.CreateUserUseCase
import com.matiasilveiro.automastichome.core.domain.User
import com.matiasilveiro.automastichome.core.ui.BaseAlertViewState
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.login.ui.navigatorstates.SignUpNavigatorStates
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpViewModel @ViewModelInject constructor(
        val createUserUseCase: CreateUserUseCase
): ViewModel() {

    private val _navigation = SingleLiveEvent<SignUpNavigatorStates>()
    val navigation: LiveData<SignUpNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseAlertViewState> = MutableLiveData()
    val viewState : LiveData<BaseAlertViewState> get() = _viewState

    fun createUser(email: String, password: String) {
        viewModelScope.launch {
            _viewState.value = BaseAlertViewState.Loading

            val user = User("",email.substringBefore("@"),email,"")
            when( val result = createUserUseCase.invoke(user,email,password) ) {
                is MyResult.Success -> {
                    _viewState.value = BaseAlertViewState.Alert("Usuario creado", "Usuario creado correctamente")
                    delay(1000)
                    goBack()
                }
                is MyResult.Failure -> {
                    _viewState.value = BaseAlertViewState.Failure(result.exception)
                }
            }
        }
    }

    fun goBack() {
        _navigation.value = SignUpNavigatorStates.GoBack
    }
}