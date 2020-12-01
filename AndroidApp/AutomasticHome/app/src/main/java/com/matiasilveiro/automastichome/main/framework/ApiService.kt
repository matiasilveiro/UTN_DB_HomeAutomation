package com.matiasilveiro.automastichome.main.framework

import com.google.gson.JsonArray
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.data.NodesDataSource
import com.matiasilveiro.automastichome.main.domain.*
import com.matiasilveiro.automastichome.main.framework.mappers.RetrofitCentralNode
import com.matiasilveiro.automastichome.main.framework.mappers.RetrofitControlFeedback
import com.matiasilveiro.automastichome.main.framework.mappers.RetrofitRemoteActuator
import com.matiasilveiro.automastichome.main.framework.mappers.RetrofitRemoteSensor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {

    @GET("/central_nodes/userId={uid}")
    suspend fun getCentralNodesByUser(@Path("uid") uid: String): Response<ArrayList<RetrofitCentralNode>>

    suspend fun createCentralNode(node: CentralNode): Response<Boolean>

    suspend fun setCentralNode(node: CentralNode): Response<Boolean>

    suspend fun deleteCentralNode(node: CentralNode): Response<Boolean>


    @GET("/remote_actuators/centralId={uid}")
    suspend fun getRemoteActuatorsByCentral(@Path("uid") uid: String): Response<ArrayList<RetrofitRemoteActuator>>

    suspend fun createRemoteActuator(node: RemoteActuator): Response<Boolean>

    suspend fun setRemoteActuator(node: RemoteActuator): Response<Boolean>

    suspend fun deleteRemoteActuator(node: RemoteActuator): Response<Boolean>


    @GET("/remote_sensors/centralId={uid}")
    suspend fun getRemoteSensorsByCentral(@Path("uid") uid: String): Response<ArrayList<RetrofitRemoteSensor>>

    suspend fun createRemoteSensor(node: RemoteSensor): Response<Boolean>

    suspend fun setRemoteSensor(node: RemoteSensor): Response<Boolean>

    suspend fun deleteRemoteSensor(node: RemoteSensor): Response<Boolean>


    suspend fun getRemoteControlsByCentral(uid: String): Response<ArrayList<RetrofitControlFeedback>?>

    suspend fun createRemoteControl(sensor: RemoteSensor, actuator: RemoteActuator, control: ControlFeedback): Response<Boolean>

    suspend fun setRemoteControl(control: ControlFeedback): Response<Boolean>

    suspend fun deleteRemoteControl(control: ControlFeedback): Response<Boolean>
}