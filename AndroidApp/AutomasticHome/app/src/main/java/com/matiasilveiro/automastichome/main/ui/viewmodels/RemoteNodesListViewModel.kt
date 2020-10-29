package com.matiasilveiro.automastichome.main.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.SingleLiveEvent
import com.matiasilveiro.automastichome.main.ui.models.RemoteNodeUI
import com.matiasilveiro.automastichome.main.ui.navigatorstates.RemoteNodesListNavigatorStates

class RemoteNodesListViewModel : ViewModel() {

    private val _navigation = SingleLiveEvent<RemoteNodesListNavigatorStates>()
    val navigation : LiveData<RemoteNodesListNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState : LiveData<BaseViewState> get() = _viewState

    private val _nodes : MutableLiveData<ArrayList<RemoteNodeUI>> = MutableLiveData()
    val nodes : LiveData<ArrayList<RemoteNodeUI>> get() = _nodes

    fun goBack() {
        _navigation.value = RemoteNodesListNavigatorStates.GoBack
    }
}