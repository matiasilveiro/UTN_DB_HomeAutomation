package com.matiasilveiro.automastichome.main.usecases

import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.data.NodesRepository
import com.matiasilveiro.automastichome.main.domain.CentralNode

class GetCentralNodesUseCase (private val nodesRepository: NodesRepository) {
    suspend operator fun invoke(uid: String): MyResult<ArrayList<CentralNode>?> {
        return nodesRepository.getCentralNodesByUser(uid)
    }
}