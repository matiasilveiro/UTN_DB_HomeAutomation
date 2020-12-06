package com.matiasilveiro.automastichome.main.ui.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manadigital.tecontactolocal.CoreFeature.usecases.GetCurrentUserUseCase
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.main.domain.RemoteActuator
import com.matiasilveiro.automastichome.main.ui.fragments.RemoteActuatorsListFragment
import com.matiasilveiro.automastichome.main.ui.navigatorstates.RemoteActuatorsListNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewstates.DataViewState
import com.matiasilveiro.automastichome.main.usecases.GetCentralNodesUseCase
import com.matiasilveiro.automastichome.main.usecases.GetRemoteNodesUseCase
import com.matiasilveiro.automastichome.main.usecases.ManageActuatorNodesUseCase
import com.matiasilveiro.automastichome.main.usecases.ManageCentralNodeUseCase
import kotlinx.coroutines.launch

class RemoteActuatorsListViewModel @ViewModelInject constructor(
    val getCurrentUserUseCase: GetCurrentUserUseCase,
    val getRemoteNodesUseCase: GetRemoteNodesUseCase,
    val manageActuatorNodesUseCase: ManageActuatorNodesUseCase
): ViewModel() {
    private val _navigation = SingleLiveEvent<RemoteActuatorsListNavigatorStates>()
    val navigation : LiveData<RemoteActuatorsListNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<DataViewState> = MutableLiveData()
    val viewState : LiveData<DataViewState> get() = _viewState

    private val _nodes : MutableLiveData<ArrayList<RemoteActuator>> = MutableLiveData()
    val nodes : LiveData<ArrayList<RemoteActuator>> get() = _nodes

    private var centralUid: String = ""


    init {
        loadNodes()
    }

    fun setCentralNode(uid: String) {
        centralUid = uid
    }

    fun refreshNodes() {
        viewModelScope.launch {
            _viewState.value = DataViewState.Refreshing
            getNodesList()
        }
    }

    fun loadNodes() {
        viewModelScope.launch {
            _viewState.value = DataViewState.Loading
            getNodesList()
        }
    }

    private suspend fun getNodesList() {
        when(val result = getRemoteNodesUseCase.getActuators(centralUid)) {
            is MyResult.Success -> {
                _nodes.value = result.data
                _viewState.value = DataViewState.Ready
            }
            is MyResult.Failure -> {
                _viewState.value = DataViewState.Failure(result.exception)
            }
        }
    }

    fun onNodeClicked(node: RemoteActuator) {
        Log.d("RemoteActuatorsList", "Node clicked: ${node.name}")
        viewModelScope.launch {
            manageActuatorNodesUseCase.create(node)
        }
    }

    fun onNodeSwitchToggled(node: RemoteActuator, state: Boolean) {
        Log.d("RemoteActuatorsList", "Node switch: ${node.name} - State: $state")
        node.value = if(state) 1 else 0
        viewModelScope.launch {
            manageActuatorNodesUseCase.setValue(node)
        }
    }

    fun goBack() {
        _navigation.value = RemoteActuatorsListNavigatorStates.GoBack
    }
}