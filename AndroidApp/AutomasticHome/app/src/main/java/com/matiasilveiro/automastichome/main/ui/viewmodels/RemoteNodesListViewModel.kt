package com.matiasilveiro.automastichome.main.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.main.ui.models.RemoteNodeUI
import com.matiasilveiro.automastichome.main.ui.navigatorstates.RemoteNodesListNavigatorStates
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoteNodesListViewModel : ViewModel() {

    private val _navigation = SingleLiveEvent<RemoteNodesListNavigatorStates>()
    val navigation : LiveData<RemoteNodesListNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState : LiveData<BaseViewState> get() = _viewState

    private val _nodes : MutableLiveData<ArrayList<RemoteNodeUI>> = MutableLiveData()
    val nodes : LiveData<ArrayList<RemoteNodeUI>> get() = _nodes

    init {
        var nodesList = arrayListOf<RemoteNodeUI>()
        nodesList.add(RemoteNodeUI("","TV","https://images.pexels.com/photos/1201996/pexels-photo-1201996.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"))
        nodesList.add(RemoteNodeUI("","Consola","https://images.pexels.com/photos/687811/pexels-photo-687811.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"))
        nodesList.add(RemoteNodeUI("","Nodo 3",""))
        nodesList.add(RemoteNodeUI("","Nodo 4",""))
        nodesList.add(RemoteNodeUI("","Nodo 5",""))
        nodesList.add(RemoteNodeUI("","Nodo 6",""))

        _nodes.value = nodesList
    }

    fun goBack() {
        _navigation.value = RemoteNodesListNavigatorStates.GoBack
    }
}