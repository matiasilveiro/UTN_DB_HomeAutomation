package com.matiasilveiro.automastichome.main.framework

import com.google.gson.JsonArray
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.data.NodesDataSource
import com.matiasilveiro.automastichome.main.domain.*
import com.matiasilveiro.automastichome.main.framework.mappers.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("/central_nodes/userId={uid}")
    suspend fun getCentralNodesByUser(@Path("uid") uid: String): Response<ArrayList<RetrofitCentralNode>>

    @GET("/central_nodes/nodeAddr={addr}")
    suspend fun getCentralNodesByAddress(@Path("addr") address: String): Response<ArrayList<RetrofitCentralNode>>

    suspend fun createCentralNode(node: CentralNode): Response<RetrofitResult>

    @POST("central_nodes/create_role")
    @FormUrlEncoded
    suspend fun createCentralNodeRole(@Field("userId") userId: String, @Field("centralId") centralId: String, @Field("role") role: Int): Response<RetrofitResult>

    @PUT("/central_nodes/userId={uid}")
    @FormUrlEncoded
    suspend fun setCentralNode(@Path("uid") uid: String, @Field("jsonNode") node: String): Response<RetrofitResult>

    suspend fun deleteCentralNode(node: CentralNode): Response<RetrofitResult>


    @GET("/remote_actuators/centralId={uid}")
    suspend fun getRemoteActuatorsByCentral(@Path("uid") uid: String): Response<ArrayList<RetrofitRemoteActuator>>

    @POST("remote_actuators/new")
    @FormUrlEncoded
    suspend fun createRemoteActuator(@Field("jsonNode") node: String): Response<RetrofitResult>

    @PUT("remote_actuators/centralId={uid}")
    @FormUrlEncoded
    suspend fun setRemoteActuator(@Path("uid") uid: String, @Field("jsonNode") node: String): Response<RetrofitResult>

    @PUT("remote_actuators/value")
    @FormUrlEncoded
    suspend fun setRemoteActuatorValue(@Field("id") id: Int, @Field("value") value: Int): Response<RetrofitResult>

    @DELETE("remote_actuators/centralId={uid}")
    @FormUrlEncoded
    suspend fun deleteRemoteActuator(@Field("nodeId") nodeId: Int): Response<RetrofitResult>


    @GET("/remote_sensors/centralId={uid}")
    suspend fun getRemoteSensorsByCentral(@Path("uid") uid: String): Response<ArrayList<RetrofitRemoteSensor>>

    suspend fun createRemoteSensor(node: RemoteSensor): Response<RetrofitResult>

    suspend fun setRemoteSensor(node: RemoteSensor): Response<RetrofitResult>

    @DELETE("remote_sensors/centralId={uid}")
    @FormUrlEncoded
    suspend fun deleteRemoteSensor(@Field("nodeId") nodeId: Int): Response<RetrofitResult>

    @GET("/controls/centralId={uid}")
    suspend fun getRemoteControlsByCentral(@Path("uid") uid: String): Response<ArrayList<RetrofitControlFeedback>?>

    @POST("controls/new")
    @FormUrlEncoded
    suspend fun createRemoteControl(@Field("actuatorId") actuatorId: Int, @Field("sensorId") sensorId: Int, @Field("jsonControl") control: String): Response<RetrofitResult>

    suspend fun setRemoteControl(control: ControlFeedback): Response<RetrofitResult>

    suspend fun deleteRemoteControl(control: ControlFeedback): Response<RetrofitResult>

    @POST("controls/bind")
    @FormUrlEncoded
    suspend fun bindRemoteControl(@Field("controlId") controlId: Int, @Field("actuatorId") actuatorId: Int, @Field("sensorId") sensorId: Int): Response<RetrofitResult>
}