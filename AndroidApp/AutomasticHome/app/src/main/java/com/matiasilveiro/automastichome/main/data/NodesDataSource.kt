package com.matiasilveiro.automastichome.main.data

import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.domain.*

interface NodesDataSource {
    suspend fun getCentralNodesByUser(uid: String): MyResult<ArrayList<CentralNode>?>
    suspend fun createCentralNode(node: CentralNode): MyResult<Boolean>
    suspend fun setCentralNode(node: CentralNode): MyResult<Boolean>
    suspend fun deleteCentralNode(node: CentralNode): MyResult<Boolean>

    suspend fun getRemoteActuatorsByCentral(uid: String): MyResult<ArrayList<RemoteActuator>?>
    suspend fun createRemoteActuator(node: RemoteActuator): MyResult<Boolean>
    suspend fun setRemoteActuator(node: RemoteActuator): MyResult<Boolean>
    suspend fun deleteRemoteActuator(node: RemoteActuator): MyResult<Boolean>

    suspend fun getRemoteSensorsByCentral(uid: String): MyResult<ArrayList<RemoteSensor>?>
    suspend fun createRemoteSensor(node: RemoteSensor): MyResult<Boolean>
    suspend fun setRemoteSensor(node: RemoteSensor): MyResult<Boolean>
    suspend fun deleteRemoteSensor(node: RemoteSensor): MyResult<Boolean>

    suspend fun getRemoteControlsByCentral(uid: String): MyResult<ArrayList<ControlFeedback>?>
    suspend fun createRemoteControl(sensor: RemoteSensor, actuator: RemoteActuator, control: ControlFeedback): MyResult<Boolean>
    suspend fun setRemoteControl(control: ControlFeedback): MyResult<Boolean>
    suspend fun deleteRemoteControl(control: ControlFeedback): MyResult<Boolean>
}