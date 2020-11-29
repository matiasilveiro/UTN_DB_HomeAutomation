package com.matiasilveiro.automastichome.main.usecases

import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.data.NodesRepository
import com.matiasilveiro.automastichome.main.domain.ControlFeedback
import com.matiasilveiro.automastichome.main.domain.RemoteActuator
import com.matiasilveiro.automastichome.main.domain.RemoteSensor

class GetRemoteNodesUseCase (private val nodesRepository: NodesRepository) {
    suspend fun getActuators(uid: String): MyResult<ArrayList<RemoteActuator>?> {
        return nodesRepository.getRemoteActuatorsByCentral(uid)
    }
    suspend fun getSensors(uid: String): MyResult<ArrayList<RemoteSensor>?> {
        return nodesRepository.getRemoteSensorsByCentral(uid)
    }
    suspend fun getControls(uid: String): MyResult<ArrayList<ControlFeedback>?> {
        return nodesRepository.getRemoteControlsByCentral(uid)
    }
}