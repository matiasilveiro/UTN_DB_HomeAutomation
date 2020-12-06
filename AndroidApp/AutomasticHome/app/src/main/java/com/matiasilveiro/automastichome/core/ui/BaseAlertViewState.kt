package com.matiasilveiro.automastichome.core.ui

sealed class BaseAlertViewState{
    object Ready: BaseAlertViewState()
    object Loading: BaseAlertViewState()
    data class Failure(val exception: Exception): BaseAlertViewState()
    data class Alert(val title: String, val message: String): BaseAlertViewState()
}
