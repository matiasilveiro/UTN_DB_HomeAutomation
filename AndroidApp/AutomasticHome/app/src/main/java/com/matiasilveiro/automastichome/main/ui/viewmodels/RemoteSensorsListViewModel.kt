package com.matiasilveiro.automastichome.main.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.main.domain.RemoteSensor
import com.matiasilveiro.automastichome.main.ui.navigatorstates.RemoteSensorsListNavigatorStates

class RemoteSensorsListViewModel : ViewModel() {
    private val _navigation = SingleLiveEvent<RemoteSensorsListNavigatorStates>()
    val navigation : LiveData<RemoteSensorsListNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState : LiveData<BaseViewState> get() = _viewState

    private val _nodes : MutableLiveData<ArrayList<RemoteSensor>> = MutableLiveData()
    val nodes : LiveData<ArrayList<RemoteSensor>> get() = _nodes

    init {
        var nodesList = arrayListOf<RemoteSensor>()
        nodesList.add(RemoteSensor("","","TV","","https://images.pexels.com/photos/1201996/pexels-photo-1201996.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940","","°C",25))
        nodesList.add(RemoteSensor("","","Consola","","https://images.pexels.com/photos/687811/pexels-photo-687811.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","","%",47))
        nodesList.add(RemoteSensor("","","Nodo 3","","","","Unidad",0))
        nodesList.add(RemoteSensor("","","Nodo 4","","","","Unidad",1))
        nodesList.add(RemoteSensor("","","Nodo 5","","","","Unidad",2))
        nodesList.add(RemoteSensor("","","Nodo 6","","","","Unidad",4))

        _nodes.value = nodesList
    }

    fun goBack() {
        _navigation.value = RemoteSensorsListNavigatorStates.GoBack
    }
}