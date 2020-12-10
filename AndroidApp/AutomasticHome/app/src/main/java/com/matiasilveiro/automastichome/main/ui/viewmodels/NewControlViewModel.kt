package com.matiasilveiro.automastichome.main.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manadigital.tecontactolocal.CoreFeature.usecases.GetCurrentUserUseCase
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.main.domain.ControlFeedback
import com.matiasilveiro.automastichome.main.domain.RemoteActuator
import com.matiasilveiro.automastichome.main.domain.RemoteSensor
import com.matiasilveiro.automastichome.main.ui.navigatorstates.NewControlNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewstates.DataViewState
import com.matiasilveiro.automastichome.main.usecases.GetRemoteNodesUseCase
import com.matiasilveiro.automastichome.main.usecases.ManageControlsUseCase
import kotlinx.coroutines.launch

class NewControlViewModel  @ViewModelInject constructor(
        val getCurrentUserUseCase: GetCurrentUserUseCase,
        val getRemoteNodesUseCase: GetRemoteNodesUseCase,
        val manageControlsUseCase: ManageControlsUseCase
): ViewModel() {

    private val _navigation = SingleLiveEvent<NewControlNavigatorStates>()
    val navigation : LiveData<NewControlNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<DataViewState> = MutableLiveData()
    val viewState : LiveData<DataViewState> get() = _viewState

    private val _actuators : MutableLiveData<ArrayList<RemoteActuator>> = MutableLiveData()
    val actuators : LiveData<ArrayList<RemoteActuator>> get() = _actuators

    private val _sensors : MutableLiveData<ArrayList<RemoteSensor>> = MutableLiveData()
    val sensors : LiveData<ArrayList<RemoteSensor>> get() = _sensors

    private val _controls : MutableLiveData<ArrayList<ControlFeedback>> = MutableLiveData()
    val controls : LiveData<ArrayList<ControlFeedback>> get() = _controls

    var centralUid: String = ""

    fun loadNodes() {
        viewModelScope.launch {
            loadActuators()
            loadSensors()
        }
    }

    private suspend fun loadActuators() {
        _viewState.value = DataViewState.Loading
        when(val result = getRemoteNodesUseCase.getActuators(centralUid)) {
            is MyResult.Success -> {
                _actuators.value = result.data
                _viewState.value = DataViewState.Ready
            }
            is MyResult.Failure -> {
                _viewState.value = DataViewState.Failure(result.exception)
            }
        }
    }

    private suspend fun loadSensors() {
        _viewState.value = DataViewState.Loading
        when(val result = getRemoteNodesUseCase.getSensors(centralUid)) {
            is MyResult.Success -> {
                _sensors.value = result.data
                _viewState.value = DataViewState.Ready
            }
            is MyResult.Failure -> {
                _viewState.value = DataViewState.Failure(result.exception)
            }
        }
    }

    fun createControl(control: ControlFeedback, sensorName: String, actuatorName: String) {
        viewModelScope.launch {
            _viewState.value = DataViewState.Loading
            val sensor = sensors.value!!.find { it.name == sensorName }
            val actuator = actuators.value!!.find { it.name == actuatorName }

            when(val result = manageControlsUseCase.create(control, actuator!!, sensor!!)) {
                is MyResult.Success -> {
                    _viewState.value = DataViewState.Ready
                    _navigation.value = NewControlNavigatorStates.GoBack
                }
                is MyResult.Failure -> {
                    _viewState.value = DataViewState.Failure(result.exception)
                }
            }
        }
    }
}