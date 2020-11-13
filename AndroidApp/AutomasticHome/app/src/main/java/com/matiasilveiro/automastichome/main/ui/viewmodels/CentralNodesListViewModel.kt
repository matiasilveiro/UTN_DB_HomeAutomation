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

    init {
        var nodesList = arrayListOf<CentralNodeUI>()
        nodesList.add(CentralNodeUI("","Mi casa","https://images.pexels.com/photos/276724/pexels-photo-276724.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"))
        nodesList.add(CentralNodeUI("","Mi oficina","https://images.pexels.com/photos/1170412/pexels-photo-1170412.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"))
        nodesList.add(CentralNodeUI("","Mi habitacion","https://images.pexels.com/photos/1267438/pexels-photo-1267438.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"))
        nodesList.add(CentralNodeUI("","Nodo 4",""))

        _nodes.value = nodesList
    }

    fun goToRemoteNodesList(centralNodeUI: CentralNodeUI) {
        _navigation.value = CentralNodesListNavigatorStates.ToRemoteNodesList(centralNodeUI)
    }

    fun goBack() {
        _navigation.value = CentralNodesListNavigatorStates.GoBack
    }
}