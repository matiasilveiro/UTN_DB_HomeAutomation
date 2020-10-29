package com.matiasilveiro.automastichome.main.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.main.ui.navigatorstates.NodeSensorNavigatorStates

class NodeSensorViewModel : ViewModel() {

    private val _navigation = SingleLiveEvent<NodeSensorNavigatorStates>()
    val navigation : LiveData<NodeSensorNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState : LiveData<BaseViewState> get() = _viewState

    fun goBack() {
        _navigation.value = NodeSensorNavigatorStates.GoBack
    }
}