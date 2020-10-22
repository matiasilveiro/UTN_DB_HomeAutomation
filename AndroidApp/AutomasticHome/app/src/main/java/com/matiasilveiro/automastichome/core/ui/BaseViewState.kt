package com.matiasilveiro.automastichome.core.ui

sealed class BaseViewState{
    object Ready: BaseViewState()
    object Loading: BaseViewState()
    data class Failure(val exception: Exception): BaseViewState()
}
