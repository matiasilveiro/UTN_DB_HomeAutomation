package com.matiasilveiro.automastichome.main.ui.viewstates

sealed class DataViewState  {
    object Ready: DataViewState()
    object Loading: DataViewState()
    object Refreshing: DataViewState()
    data class Failure(val exception: Exception): DataViewState()
}