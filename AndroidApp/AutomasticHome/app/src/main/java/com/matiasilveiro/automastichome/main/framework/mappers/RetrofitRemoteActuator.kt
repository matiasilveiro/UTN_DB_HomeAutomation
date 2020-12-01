package com.matiasilveiro.automastichome.main.framework.mappers

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.matiasilveiro.automastichome.main.domain.RemoteActuator

@Keep
class RetrofitRemoteActuator(
    @SerializedName("NodeId") val uid: String,
    @SerializedName("CentralId") val centralUid: String,
    @SerializedName("Name") val name: String,
    @SerializedName("Address") val address: String,
    @SerializedName("ImageUrl") val imageUrl: String?,
    @SerializedName("Status") val status: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Value") val value: Int)

fun RetrofitRemoteActuator.toRemoteActuator(): RemoteActuator = RemoteActuator(uid,centralUid,name,address,imageUrl?:"",status,type,value)
fun RemoteActuator.toRetrofitRemoteActuator(): RetrofitRemoteActuator = RetrofitRemoteActuator(uid,centralUid,name,address,imageUrl,status,type,value)