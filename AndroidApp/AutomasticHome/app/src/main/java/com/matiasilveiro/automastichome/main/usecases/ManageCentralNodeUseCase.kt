package com.matiasilveiro.automastichome.main.usecases

import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.data.NodesRepository
import com.matiasilveiro.automastichome.main.domain.CentralNode

class ManageCentralNodeUseCase (private val nodesRepository: NodesRepository) {
    suspend fun create(node: CentralNode): MyResult<Boolean> {
        return nodesRepository.createCentralNode(node)
    }
    suspend fun set(node: CentralNode): MyResult<Boolean> {
        return nodesRepository.setCentralNode(node)
    }
    suspend fun delete(node: CentralNode): MyResult<Boolean> {
        return nodesRepository.deleteCentralNode(node)
    }
}