package com.matiasilveiro.automastichome.main.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.main.domain.CentralNode
import com.matiasilveiro.automastichome.main.ui.navigatorstates.EditCentralNodeNavigatorStates
import com.matiasilveiro.automastichome.main.usecases.GetCentralNodesUseCase
import com.matiasilveiro.automastichome.main.usecases.ManageCentralNodeUseCase
import kotlinx.coroutines.launch

class EditCentralNodeViewModel @ViewModelInject constructor(
    val getCentralNodesUseCase: GetCentralNodesUseCase,
    val manageCentralNodeUseCase: ManageCentralNodeUseCase
): ViewModel() {

    private val _navigation = SingleLiveEvent<EditCentralNodeNavigatorStates>()
    val navigation : LiveData<EditCentralNodeNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState : LiveData<BaseViewState> get() = _viewState

    fun saveChanges(centralNode: CentralNode) {
        viewModelScope.launch {
            _viewState.value = BaseViewState.Loading
            when(val result = manageCentralNodeUseCase.set(centralNode) ) {
                is MyResult.Success -> {
                    _navigation.value = EditCentralNodeNavigatorStates.GoBack
                    _viewState.value = BaseViewState.Ready
                }
                is MyResult.Failure -> {
                    _viewState.value = BaseViewState.Failure(result.exception)
                }
            }
        }
    }

    fun goBack() {
        _navigation.value = EditCentralNodeNavigatorStates.GoBack
    }
}