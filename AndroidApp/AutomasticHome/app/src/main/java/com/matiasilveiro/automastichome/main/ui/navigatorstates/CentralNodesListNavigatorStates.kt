package com.matiasilveiro.automastichome.main.ui.navigatorstates

import com.matiasilveiro.automastichome.main.ui.models.CentralNodeUI

sealed class CentralNodesListNavigatorStates {
    data class ToRemoteNodesList(val centralNode: CentralNodeUI): CentralNodesListNavigatorStates()
    data class ToEditCentralNode(val centralNode: CentralNodeUI): CentralNodesListNavigatorStates()
    object GoBack: CentralNodesListNavigatorStates()
}