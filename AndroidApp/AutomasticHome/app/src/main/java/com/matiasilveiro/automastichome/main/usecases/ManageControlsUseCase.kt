package com.matiasilveiro.automastichome.main.usecases

import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.data.NodesRepository
import com.matiasilveiro.automastichome.main.domain.ControlFeedback
import com.matiasilveiro.automastichome.main.domain.RemoteActuator
import com.matiasilveiro.automastichome.main.domain.RemoteSensor

class ManageControlsUseCase (private val nodesRepository: NodesRepository) {
    suspend fun create(control: ControlFeedback, actuator: RemoteActuator, sensor: RemoteSensor): MyResult<Boolean> {
        return nodesRepository.createRemoteControl(sensor,actuator,control)
    }
    suspend fun set(control: ControlFeedback): MyResult<Boolean> {
        return nodesRepository.setRemoteControl(control)
    }
    suspend fun delete(control: ControlFeedback): MyResult<Boolean> {
        return nodesRepository.deleteRemoteControl(control)
    }
}