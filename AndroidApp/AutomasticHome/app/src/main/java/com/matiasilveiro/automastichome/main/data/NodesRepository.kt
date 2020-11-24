package com.matiasilveiro.automastichome.main.data

import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.domain.*
import com.matiasilveiro.automastichome.main.framework.FirebaseNodesSource
import javax.inject.Inject

class NodesRepository @Inject constructor(
        private val firebaseDataSource: FirebaseNodesSource
) : NodesDataSource{

    override suspend fun getCentralNodesByUser(uid: String): MyResult<ArrayList<CentralNode>?> {
        return firebaseDataSource.getCentralNodesByUser(uid)
    }

    override suspend fun createCentralNode(node: CentralNode): MyResult<Boolean> {
        return firebaseDataSource.createCentralNode(node)
    }

    override suspend fun setCentralNode(node: CentralNode): MyResult<Boolean> {
        return firebaseDataSource.setCentralNode(node)
    }

    override suspend fun deleteCentralNode(node: CentralNode): MyResult<Boolean> {
        return firebaseDataSource.deleteCentralNode(node)
    }

    override suspend fun getRemoteActuatorsByCentral(uid: String): MyResult<ArrayList<RemoteActuator>?> {
        return firebaseDataSource.getRemoteActuatorsByCentral(uid)
    }

    override suspend fun createRemoteActuator(node: RemoteActuator): MyResult<Boolean> {
        return firebaseDataSource.createRemoteActuator(node)
    }

    override suspend fun setRemoteActuator(node: RemoteActuator): MyResult<Boolean> {
        return firebaseDataSource.setRemoteActuator(node)
    }

    override suspend fun deleteRemoteActuator(node: RemoteActuator): MyResult<Boolean> {
        return firebaseDataSource.deleteRemoteActuator(node)
    }

    override suspend fun getRemoteSensorsByCentral(uid: String): MyResult<ArrayList<RemoteSensor>?> {
        return firebaseDataSource.getRemoteSensorsByCentral(uid)
    }

    override suspend fun createRemoteSensor(node: RemoteSensor): MyResult<Boolean> {
        return firebaseDataSource.createRemoteSensor(node)
    }

    override suspend fun setRemoteSensor(node: RemoteSensor): MyResult<Boolean> {
        return firebaseDataSource.setRemoteSensor(node)
    }

    override suspend fun deleteRemoteSensor(node: RemoteSensor): MyResult<Boolean> {
        return firebaseDataSource.deleteRemoteSensor(node)
    }

    override suspend fun getRemoteControlsByCentral(uid: String): MyResult<ArrayList<ControlFeedback>?> {
        return firebaseDataSource.getRemoteControlsByCentral(uid)
    }

    override suspend fun createRemoteControl(sensor: RemoteSensor, actuator: RemoteActuator, control: ControlFeedback): MyResult<Boolean> {
        return firebaseDataSource.createRemoteControl(sensor, actuator, control)
    }

    override suspend fun setRemoteControl(control: ControlFeedback): MyResult<Boolean> {
        return firebaseDataSource.setRemoteControl(control)
    }

    override suspend fun deleteRemoteControl(control: ControlFeedback): MyResult<Boolean> {
        return firebaseDataSource.deleteRemoteControl(control)
    }

}