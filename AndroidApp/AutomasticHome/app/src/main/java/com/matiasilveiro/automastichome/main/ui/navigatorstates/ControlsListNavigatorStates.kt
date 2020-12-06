package com.matiasilveiro.automastichome.main.ui.navigatorstates

import com.matiasilveiro.automastichome.main.domain.ControlFeedback

sealed class ControlsListNavigatorStates {
    object ToNewControl: ControlsListNavigatorStates()
    data class ToEditControl(val control: ControlFeedback): ControlsListNavigatorStates()
    object GoBack: ControlsListNavigatorStates()
}