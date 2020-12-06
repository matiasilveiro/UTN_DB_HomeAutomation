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
import com.matiasilveiro.automastichome.main.domain.RemoteSensor
import com.matiasilveiro.automastichome.main.ui.navigatorstates.RemoteSensorsListNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewstates.DataViewState
import com.matiasilveiro.automastichome.main.usecases.GetRemoteNodesUseCase
import kotlinx.coroutines.launch

class RemoteSensorsListViewModel @ViewModelInject constructor(
    val getCurrentUserUseCase: GetCurrentUserUseCase,
    val getRemoteNodesUseCase: GetRemoteNodesUseCase,
): ViewModel() {
    private val _navigation = SingleLiveEvent<RemoteSensorsListNavigatorStates>()
    val navigation : LiveData<RemoteSensorsListNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<DataViewState> = MutableLiveData()
    val viewState : LiveData<DataViewState> get() = _viewState

    private val _nodes : MutableLiveData<ArrayList<RemoteSensor>> = MutableLiveData()
    val nodes : LiveData<ArrayList<RemoteSensor>> get() = _nodes

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
        viewModelScope.launch {
            when(val result = getRemoteNodesUseCase.getSensors(centralUid)) {
                is MyResult.Success -> {
                    _nodes.value = result.data
                    _viewState.value = DataViewState.Ready
                }
                is MyResult.Failure -> {
                    _viewState.value = DataViewState.Failure(result.exception)
                }
            }
        }
    }

    fun onNodeClicked(node: RemoteActuator) {
        Log.d("RemoteActuatorsList", "Node clicked: ${node.name}")
    }

    fun goBack() {
        _navigation.value = RemoteSensorsListNavigatorStates.GoBack
    }
}