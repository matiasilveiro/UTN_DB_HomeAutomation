package com.matiasilveiro.automastichome.main.usecases

import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.data.NodesRepository
import com.matiasilveiro.automastichome.main.domain.RemoteActuator

class ManageActuatorNodesUseCase (private val nodesRepository: NodesRepository) {
    suspend fun create(node: RemoteActuator): MyResult<Boolean> {
        return nodesRepository.createRemoteActuator(node)
    }
    suspend fun set(node: RemoteActuator): MyResult<Boolean> {
        return nodesRepository.setRemoteActuator(node)
    }
    suspend fun setValue(node: RemoteActuator): MyResult<Boolean> {
        return nodesRepository.setRemoteActuatorValue(node)
    }
    suspend fun delete(node: RemoteActuator): MyResult<Boolean> {
        return nodesRepository.deleteRemoteActuator(node)
    }
}