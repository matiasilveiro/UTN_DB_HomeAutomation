package com.matiasilveiro.automastichome.main.framework

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.data.NodesDataSource
import com.matiasilveiro.automastichome.main.domain.*
import com.matiasilveiro.automastichome.main.framework.mappers.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseNodesSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : NodesDataSource{

    companion object Collections {
        const val ROLES = "Roles"
        const val CENTRAL_NODES = "CentralNodes"
        const val REMOTE_ACTUATORS = "RemoteActuators"
        const val REMOTE_SENSORS = "RemoteSensors"
        const val SENS_ACT_REL = "SensorActuatorRelations"
        const val CONTROL = "ControlFeedback"
    }

    override suspend fun getCentralNodesByUser(uid: String): MyResult<ArrayList<CentralNode>?> {
         try {
            val documents = db.collection(ROLES).whereEqualTo("userUid",uid).get().await()
            if(!documents.isEmpty) {
                val nodesList = mutableListOf<CentralNode>()
                documents.forEach { document ->
                    Log.d("FirebaseNodesSource", "Role retrieved with uid ${document.id}")
                    val role = document.toObject<FirebaseNodeUserRole>()

                    val dbNode = db.collection(CENTRAL_NODES).document(role.centralUid).get().await()
                    Log.d("FirebaseNodesSource", "Central node retrieved with uid ${dbNode.id}")
                    dbNode.toObject<FirebaseCentralNode>()?.let {
                        val node = it.toCentralNode()
                        node.role = role.role
                        nodesList.add(node)
                    }
                }
                return MyResult.Success(ArrayList(nodesList))
            } else {
                return MyResult.Success(null)
            }
        } catch (e: Exception) {
            return MyResult.Failure(e)
        }
    }

    override suspend fun createCentralNode(node: CentralNode): MyResult<Boolean> {
        return try {
            val reference = db.collection(CENTRAL_NODES).add(node).await()
            node.uid = reference.id
            db.collection(CENTRAL_NODES).document(reference.id).set(node).await()

            Log.d("FirebaseNodesSource", "Central node created with uid ${reference.id}")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseNodesSource", e)
            MyResult.Failure(e)
        }
    }

    override suspend fun setCentralNode(node: CentralNode): MyResult<Boolean> {
        return try {
            val reference = db.collection(CENTRAL_NODES).document(node.uid).set(node).await()

            Log.d("FirebaseNodesSource", "Central node modified with uid ${node.uid}")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseNodesSource", e)
            MyResult.Failure(e)
        }
    }

    override suspend fun deleteCentralNode(node: CentralNode): MyResult<Boolean> {
        return try {
            val reference = db.collection(CENTRAL_NODES).document(node.uid).delete().await()

            Log.d("FirebaseNodesSource", "Central node deleted with uid ${node.uid}")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseNodesSource", e)
            MyResult.Failure(e)
        }
    }


    override suspend fun getRemoteActuatorsByCentral(uid: String): MyResult<ArrayList<RemoteActuator>?> {
        return try {
            val documents = db.collection(REMOTE_ACTUATORS).whereEqualTo("centralUid",uid).get().await()
            if(!documents.isEmpty) {
                val nodesList = mutableListOf<RemoteActuator>()
                documents.forEach { document ->
                    Log.d("FirebaseShopSource", "Remote node retrieved with uid ${document.id}")
                    nodesList.add(document.toObject<FirebaseRemoteActuator>().toRemoteActuator())
                }
                MyResult.Success(ArrayList(nodesList))
            } else {
                MyResult.Success(null)
            }
        } catch (e: Exception) {
            MyResult.Failure(e)
        }
    }

    override suspend fun createRemoteActuator(node: RemoteActuator): MyResult<Boolean> {
        return try {
            val reference = db.collection(REMOTE_ACTUATORS).add(node).await()
            node.uid = reference.id
            db.collection(REMOTE_ACTUATORS).document(reference.id).set(node).await()

            Log.d("FirebaseNodesSource", "Remote node created with uid ${reference.id}")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseNodesSource", e)
            MyResult.Failure(e)
        }
    }

    override suspend fun setRemoteActuator(node: RemoteActuator): MyResult<Boolean> {
        return try {
            val reference = db.collection(REMOTE_ACTUATORS).document(node.uid).set(node).await()

            Log.d("FirebaseNodesSource", "Remote node modified with uid ${node.uid}")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseNodesSource", e)
            MyResult.Failure(e)
        }
    }

    override suspend fun setRemoteActuatorValue(node: RemoteActuator): MyResult<Boolean> {
        return try {
            val reference = db.collection(REMOTE_ACTUATORS).document(node.uid).set(node).await()

            Log.d("FirebaseNodesSource", "Remote node modified with uid ${node.uid}")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseNodesSource", e)
            MyResult.Failure(e)
        }
    }

    override suspend fun deleteRemoteActuator(node: RemoteActuator): MyResult<Boolean> {
        return try {
            val reference = db.collection(REMOTE_ACTUATORS).document(node.uid).delete().await()

            Log.d("FirebaseNodesSource", "Remote node deleted with uid ${node.uid}")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseNodesSource", e)
            MyResult.Failure(e)
        }
    }

    override suspend fun getRemoteSensorsByCentral(uid: String): MyResult<ArrayList<RemoteSensor>?> {
        return try {
            val documents = db.collection(REMOTE_SENSORS).whereEqualTo("centralUid",uid).get().await()
            if(!documents.isEmpty) {
                val nodesList = mutableListOf<RemoteSensor>()
                documents.forEach { document ->
                    Log.d("FirebaseShopSource", "Remote node retrieved with uid ${document.id}")
                    nodesList.add(document.toObject<FirebaseRemoteSensor>().toRemoteSensor())
                }
                MyResult.Success(ArrayList(nodesList))
            } else {
                MyResult.Success(null)
            }
        } catch (e: Exception) {
            MyResult.Failure(e)
        }
    }

    override suspend fun createRemoteSensor(node: RemoteSensor): MyResult<Boolean> {
        return try {
            val reference = db.collection(REMOTE_SENSORS).add(node).await()
            node.uid = reference.id
            db.collection(REMOTE_SENSORS).document(reference.id).set(node).await()

            Log.d("FirebaseNodesSource", "Remote node created with uid ${reference.id}")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseNodesSource", e)
            MyResult.Failure(e)
        }
    }

    override suspend fun setRemoteSensor(node: RemoteSensor): MyResult<Boolean> {
        return try {
            val reference = db.collection(REMOTE_SENSORS).document(node.uid).set(node).await()

            Log.d("FirebaseNodesSource", "Remote node modified with uid ${node.uid}")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseNodesSource", e)
            MyResult.Failure(e)
        }
    }

    override suspend fun deleteRemoteSensor(node: RemoteSensor): MyResult<Boolean> {
        return try {
            val reference = db.collection(REMOTE_SENSORS).document(node.uid).delete().await()

            Log.d("FirebaseNodesSource", "Remote node deleted with uid ${node.uid}")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseNodesSource", e)
            MyResult.Failure(e)
        }
    }

    override suspend fun getRemoteControlsByCentral(uid: String): MyResult<ArrayList<ControlFeedback>?> {
        try {
            val documents = db.collection(SENS_ACT_REL).whereEqualTo("centralUid",uid).get().await()
            if(!documents.isEmpty) {
                val controlsList = mutableListOf<ControlFeedback>()
                documents.forEach { document ->
                    Log.d("FirebaseNodesSource", "Role retrieved with uid ${document.id}")
                    val relation = document.toObject<FirebaseSensorActuatorRelation>()

                    val control = db.collection(CONTROL).document(relation.controlUid).get().await()
                    Log.d("FirebaseNodesSource", "Control feedback retrieved with uid ${control.id}")
                    control.toObject<FirebaseControlFeedback>()?.let {
                        controlsList.add(it.toControlFeedback())
                    }
                }
                return MyResult.Success(ArrayList(controlsList))
            } else {
                return MyResult.Success(null)
            }
        } catch (e: Exception) {
            return MyResult.Failure(e)
        }
    }

    override suspend fun createRemoteControl(sensor: RemoteSensor, actuator: RemoteActuator, control: ControlFeedback): MyResult<Boolean> {
        return try {
            var reference = db.collection(CONTROL).add(control).await()
            control.uid = reference.id
            db.collection(CONTROL).document(reference.id).set(control).await()

            val relation = FirebaseSensorActuatorRelation("",sensor.centralUid,actuator.uid,sensor.uid,control.uid)
            reference = db.collection(SENS_ACT_REL).add(relation).await()
            relation.uid = reference.id
            db.collection(SENS_ACT_REL).document(reference.id).set(relation).await()

            Log.d("FirebaseNodesSource", "Remote control created with uid ${reference.id}")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseNodesSource", e)
            MyResult.Failure(e)
        }
    }

    override suspend fun setRemoteControl(control: ControlFeedback): MyResult<Boolean> {
        return try {
            val reference = db.collection(CONTROL).document(control.uid).set(control).await()

            Log.d("FirebaseNodesSource", "Control modified with uid ${control.uid}")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseNodesSource", e)
            MyResult.Failure(e)
        }
    }

    override suspend fun deleteRemoteControl(control: ControlFeedback): MyResult<Boolean> {
        return try {
            val reference = db.collection(CONTROL).document(control.uid).delete().await()

            Log.d("FirebaseNodesSource", "Control deleted with uid ${control.uid}")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseNodesSource", e)
            MyResult.Failure(e)
        }
    }
}