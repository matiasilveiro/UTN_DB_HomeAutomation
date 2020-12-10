package com.matiasilveiro.automastichome.main.ui.viewmodels

import android.service.controls.Control
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manadigital.tecontactolocal.CoreFeature.usecases.GetCurrentUserUseCase
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.main.domain.CentralNode
import com.matiasilveiro.automastichome.main.domain.ControlFeedback
import com.matiasilveiro.automastichome.main.ui.navigatorstates.CentralNodesListNavigatorStates
import com.matiasilveiro.automastichome.main.ui.navigatorstates.ControlsListNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewstates.DataViewState
import com.matiasilveiro.automastichome.main.usecases.GetCentralNodesUseCase
import com.matiasilveiro.automastichome.main.usecases.GetControlsUseCase
import com.matiasilveiro.automastichome.main.usecases.ManageControlsUseCase
import kotlinx.coroutines.launch

class ControlsListViewModel @ViewModelInject constructor(
        val getCurrentUserUseCase: GetCurrentUserUseCase,
        val getCentralNodesUseCase: GetCentralNodesUseCase,
        val getControlsUseCase: GetControlsUseCase,
        val manageControlsUseCase: ManageControlsUseCase
): ViewModel() {

    private val _navigation = SingleLiveEvent<ControlsListNavigatorStates>()
    val navigation : LiveData<ControlsListNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<DataViewState> = MutableLiveData()
    val viewState : LiveData<DataViewState> get() = _viewState

    private val _nodes : MutableLiveData<ArrayList<CentralNode>> = MutableLiveData()
    val nodes : LiveData<ArrayList<CentralNode>> get() = _nodes

    private val _controls : MutableLiveData<ArrayList<ControlFeedback>> = MutableLiveData()
    val controls : LiveData<ArrayList<ControlFeedback>> get() = _controls

    private lateinit var selectedNode: CentralNode

    init {
        loadNodes()
    }

    fun setCentralNode(node: CentralNode) {
        selectedNode = node
        loadControls()
    }

    fun refreshControls() {
        viewModelScope.launch {
            _viewState.value = DataViewState.Refreshing
            getControlsList()
        }
    }

    fun loadControls() {
        viewModelScope.launch {
            _viewState.value = DataViewState.Loading
            getControlsList()
        }
    }

    fun refreshNodes() {
        viewModelScope.launch {
            _viewState.value = DataViewState.Refreshing
            getNodes()
        }
    }

    fun loadNodes() {
        viewModelScope.launch {
            _viewState.value = DataViewState.Loading
            getNodes()
        }
    }

    private suspend fun getNodes() {
        when(val result = getCurrentUserUseCase()) {
            is MyResult.Success -> {
                val user = result.data!!
                when(val nodes = getCentralNodesUseCase(user.uid)) {
                    is MyResult.Success -> {
                        _nodes.value = nodes.data
                        _viewState.value = DataViewState.Ready
                    }
                    is MyResult.Failure -> {
                        _viewState.value = DataViewState.Failure(nodes.exception)
                    }
                }
            }
            is MyResult.Failure -> {
                _viewState.value = DataViewState.Failure(result.exception)
            }
        }
    }

    private suspend fun getControlsList() {
        when(val result = getControlsUseCase(selectedNode.uid)) {
            is MyResult.Success -> {
                _controls.value = result.data
                _viewState.value = DataViewState.Ready
            }
            is MyResult.Failure -> {
                _viewState.value = DataViewState.Failure(result.exception)
            }
        }
    }

    fun goToNewControl() {
        _navigation.value = ControlsListNavigatorStates.ToNewControl(selectedNode.uid)
    }

    fun goToEditControl(control: ControlFeedback) {
        _navigation.value = ControlsListNavigatorStates.ToEditControl(selectedNode.uid,control)
    }

    fun goBack() {
        _navigation.value = ControlsListNavigatorStates.GoBack
    }
}