package com.matiasilveiro.automastichome.main.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manadigital.tecontactolocal.CoreFeature.usecases.GetCurrentUserUseCase
import com.matiasilveiro.automastichome.core.domain.User
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.main.domain.CentralNode
import com.matiasilveiro.automastichome.main.ui.navigatorstates.ConnectToCentralNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewstates.DataViewState
import com.matiasilveiro.automastichome.main.usecases.CreateCentralNodeRoleUseCase
import com.matiasilveiro.automastichome.main.usecases.ManageCentralNodeUseCase
import kotlinx.coroutines.launch

class ConnectToCentralViewModel @ViewModelInject constructor(
        val getCurrentUserUseCase: GetCurrentUserUseCase,
        val createCentralNodeRoleUseCase: CreateCentralNodeRoleUseCase,
        val manageCentralNodeUseCase: ManageCentralNodeUseCase
): ViewModel() {

    private val _navigation = SingleLiveEvent<ConnectToCentralNavigatorStates>()
    val navigation : LiveData<ConnectToCentralNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState : LiveData<BaseViewState> get() = _viewState

    private val _node : MutableLiveData<CentralNode> = MutableLiveData()
    val node : LiveData<CentralNode> get() = _node

    private lateinit var user: User

    init {
        viewModelScope.launch {
            when(val result = getCurrentUserUseCase()) {
                is MyResult.Success -> {
                    user = result.data!!
                }
                is MyResult.Failure -> {
                    _viewState.value = BaseViewState.Failure(result.exception)
                }
            }
        }
    }

    fun searchNode(address: String) {
        viewModelScope.launch {
            _viewState.value = BaseViewState.Loading

            when( val result = manageCentralNodeUseCase.search(address) ) {
                is MyResult.Success -> {
                    result.data?.let {
                        _node.value = it[0]
                    }
                    _viewState.value = BaseViewState.Ready
                }
                is MyResult.Failure -> {
                    _viewState.value = BaseViewState.Failure(result.exception)
                }
            }
        }
    }

    fun createRole(role: Int) {
        viewModelScope.launch {
            _viewState.value = BaseViewState.Loading

            when( val result = createCentralNodeRoleUseCase.invoke(user.uid, node.value!!.uid, role) ) {
                is MyResult.Success -> {
                    _viewState.value = BaseViewState.Ready
                    goBack()
                }
                is MyResult.Failure -> {
                    _viewState.value = BaseViewState.Failure(result.exception)
                }
            }
        }
    }

    fun goBack() {
        _navigation.value = ConnectToCentralNavigatorStates.GoBack
    }
}