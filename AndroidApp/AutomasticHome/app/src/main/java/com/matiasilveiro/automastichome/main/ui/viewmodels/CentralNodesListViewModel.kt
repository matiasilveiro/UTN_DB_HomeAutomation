package com.matiasilveiro.automastichome.main.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manadigital.tecontactolocal.CoreFeature.usecases.GetCurrentUserUseCase
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.main.domain.CentralNode
import com.matiasilveiro.automastichome.main.ui.navigatorstates.CentralNodesListNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewstates.DataViewState
import com.matiasilveiro.automastichome.main.usecases.GetCentralNodesUseCase
import com.matiasilveiro.automastichome.main.usecases.ManageCentralNodeUseCase
import kotlinx.coroutines.launch

class CentralNodesListViewModel  @ViewModelInject constructor(
    val getCurrentUserUseCase: GetCurrentUserUseCase,
    val getCentralNodesUseCase: GetCentralNodesUseCase,
    val manageCentralNodeUseCase: ManageCentralNodeUseCase
): ViewModel() {

    private val _navigation = SingleLiveEvent<CentralNodesListNavigatorStates>()
    val navigation : LiveData<CentralNodesListNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<DataViewState> = MutableLiveData()
    val viewState : LiveData<DataViewState> get() = _viewState

    private val _nodes : MutableLiveData<ArrayList<CentralNode>> = MutableLiveData()
    val nodes : LiveData<ArrayList<CentralNode>> get() = _nodes

    init {
        loadNodes()
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

    fun goToRemoteNodesList(centralNode: CentralNode) {
        _navigation.value = CentralNodesListNavigatorStates.ToRemoteNodesList(centralNode)
    }

    fun goToEditCentralNode(centralNode: CentralNode) {
        _navigation.value = CentralNodesListNavigatorStates.ToEditCentralNode(centralNode)
    }

    fun goBack() {
        _navigation.value = CentralNodesListNavigatorStates.GoBack
    }
}