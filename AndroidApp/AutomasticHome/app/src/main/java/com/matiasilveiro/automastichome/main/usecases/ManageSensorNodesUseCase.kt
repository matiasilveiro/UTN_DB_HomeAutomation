package com.matiasilveiro.automastichome.main.usecases

import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.data.NodesRepository
import com.matiasilveiro.automastichome.main.domain.RemoteSensor

class ManageSensorNodesUseCase (private val nodesRepository: NodesRepository) {
    suspend fun create(node: RemoteSensor): MyResult<Boolean> {
        return nodesRepository.createRemoteSensor(node)
    }
    suspend fun set(node: RemoteSensor): MyResult<Boolean> {
        return nodesRepository.setRemoteSensor(node)
    }
    suspend fun delete(node: RemoteSensor): MyResult<Boolean> {
        return nodesRepository.deleteRemoteSensor(node)
    }
}