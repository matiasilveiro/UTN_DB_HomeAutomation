package com.matiasilveiro.automastichome.main.data

import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.domain.*
import com.matiasilveiro.automastichome.main.framework.FirebaseNodesSource
import com.matiasilveiro.automastichome.main.framework.RetrofitNodesSource
import javax.inject.Inject

class NodesRepository @Inject constructor(
        private val firebaseDataSource: FirebaseNodesSource,
        private val retrofitNodesSource: RetrofitNodesSource
) : NodesDataSource {

    val source = retrofitNodesSource

    override suspend fun getCentralNodesByUser(uid: String): MyResult<ArrayList<CentralNode>?> {
        return source.getCentralNodesByUser(uid)
    }

    override suspend fun createCentralNode(node: CentralNode): MyResult<Boolean> {
        return source.createCentralNode(node)
    }

    override suspend fun setCentralNode(node: CentralNode): MyResult<Boolean> {
        return source.setCentralNode(node)
    }

    override suspend fun deleteCentralNode(node: CentralNode): MyResult<Boolean> {
        return source.deleteCentralNode(node)
    }

    override suspend fun getRemoteActuatorsByCentral(uid: String): MyResult<ArrayList<RemoteActuator>?> {
        return source.getRemoteActuatorsByCentral(uid)
    }

    override suspend fun createRemoteActuator(node: RemoteActuator): MyResult<Boolean> {
        return source.createRemoteActuator(node)
    }

    override suspend fun setRemoteActuator(node: RemoteActuator): MyResult<Boolean> {
        return source.setRemoteActuator(node)
    }

    override suspend fun setRemoteActuatorValue(node: RemoteActuator): MyResult<Boolean> {
        return source.setRemoteActuatorValue(node)
    }

    override suspend fun deleteRemoteActuator(node: RemoteActuator): MyResult<Boolean> {
        return source.deleteRemoteActuator(node)
    }

    override suspend fun getRemoteSensorsByCentral(uid: String): MyResult<ArrayList<RemoteSensor>?> {
        return source.getRemoteSensorsByCentral(uid)
    }

    override suspend fun createRemoteSensor(node: RemoteSensor): MyResult<Boolean> {
        return source.createRemoteSensor(node)
    }

    override suspend fun setRemoteSensor(node: RemoteSensor): MyResult<Boolean> {
        return source.setRemoteSensor(node)
    }

    override suspend fun deleteRemoteSensor(node: RemoteSensor): MyResult<Boolean> {
        return source.deleteRemoteSensor(node)
    }

    override suspend fun getRemoteControlsByCentral(uid: String): MyResult<ArrayList<ControlFeedback>?> {
        return source.getRemoteControlsByCentral(uid)
    }

    override suspend fun createRemoteControl(sensor: RemoteSensor, actuator: RemoteActuator, control: ControlFeedback): MyResult<Boolean> {
        return source.createRemoteControl(sensor, actuator, control)
    }

    override suspend fun setRemoteControl(control: ControlFeedback): MyResult<Boolean> {
        return source.setRemoteControl(control)
    }

    override suspend fun deleteRemoteControl(control: ControlFeedback): MyResult<Boolean> {
        return source.deleteRemoteControl(control)
    }

}