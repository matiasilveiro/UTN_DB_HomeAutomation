package com.matiasilveiro.automastichome.main.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.main.ui.models.CentralNodeUI
import com.matiasilveiro.automastichome.main.ui.navigatorstates.CentralNodesListNavigatorStates

class CentralNodesListViewModel : ViewModel() {

    private val _navigation = SingleLiveEvent<CentralNodesListNavigatorStates>()
    val navigation : LiveData<CentralNodesListNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState : LiveData<BaseViewState> get() = _viewState

    private val _nodes : MutableLiveData<ArrayList<CentralNodeUI>> = MutableLiveData()
    val nodes : LiveData<ArrayList<CentralNodeUI>> get() = _nodes

    fun goBack() {
        _navigation.value = CentralNodesListNavigatorStates.GoBack
    }
}