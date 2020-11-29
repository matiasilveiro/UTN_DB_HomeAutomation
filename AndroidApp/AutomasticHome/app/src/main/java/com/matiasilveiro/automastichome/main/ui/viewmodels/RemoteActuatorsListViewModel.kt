package com.matiasilveiro.automastichome.main.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.main.domain.RemoteActuator
import com.matiasilveiro.automastichome.main.ui.navigatorstates.RemoteActuatorsListNavigatorStates

class RemoteActuatorsListViewModel : ViewModel() {
    private val _navigation = SingleLiveEvent<RemoteActuatorsListNavigatorStates>()
    val navigation : LiveData<RemoteActuatorsListNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState : LiveData<BaseViewState> get() = _viewState

    private val _nodes : MutableLiveData<ArrayList<RemoteActuator>> = MutableLiveData()
    val nodes : LiveData<ArrayList<RemoteActuator>> get() = _nodes

    init {
        var nodesList = arrayListOf<RemoteActuator>()
        nodesList.add(RemoteActuator("","","TV","","https://images.pexels.com/photos/1201996/pexels-photo-1201996.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940","","",0))
        nodesList.add(RemoteActuator("","","Consola","","https://images.pexels.com/photos/687811/pexels-photo-687811.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","","",0))
        nodesList.add(RemoteActuator("","","Nodo 3","","","","",0))
        nodesList.add(RemoteActuator("","","Nodo 4","","","","",0))
        nodesList.add(RemoteActuator("","","Nodo 5","","","","",0))
        nodesList.add(RemoteActuator("","","Nodo 6","","","","",0))

        _nodes.value = nodesList
    }

    fun goBack() {
        _navigation.value = RemoteActuatorsListNavigatorStates.GoBack
    }
}