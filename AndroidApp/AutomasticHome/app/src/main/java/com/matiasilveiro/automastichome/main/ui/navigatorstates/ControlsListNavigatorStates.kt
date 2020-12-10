package com.matiasilveiro.automastichome.main.ui.navigatorstates

import com.matiasilveiro.automastichome.main.domain.ControlFeedback

sealed class ControlsListNavigatorStates {
    data class ToNewControl(val centralUid: String): ControlsListNavigatorStates()
    data class ToEditControl(val centralUid: String, val control: ControlFeedback): ControlsListNavigatorStates()
    object GoBack: ControlsListNavigatorStates()
}