package com.matiasilveiro.automastichome.main.ui.navigatorstates

import com.matiasilveiro.automastichome.main.domain.CentralNode

sealed class CentralNodesListNavigatorStates {
    data class ToRemoteNodesList(val centralNode: CentralNode): CentralNodesListNavigatorStates()
    data class ToEditCentralNode(val centralNode: CentralNode): CentralNodesListNavigatorStates()
    object GoBack: CentralNodesListNavigatorStates()
}