package com.matiasilveiro.automastichome.main.framework.mappers

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.matiasilveiro.automastichome.main.domain.RemoteSensor

@Keep
class RetrofitRemoteSensor(
    @SerializedName("NodeId") val uid: String,
    @SerializedName("CentralId") val centralUid: String,
    @SerializedName("Name") val name: String,
    @SerializedName("Address") val address: String,
    @SerializedName("ImageUrl") val imageUrl: String?,
    @SerializedName("Status") val status: String,
    @SerializedName("Unit") val unit: String,
    @SerializedName("Value") val value: Int)

fun RetrofitRemoteSensor.toRemoteSensor(): RemoteSensor = RemoteSensor(uid,centralUid,name,address,imageUrl?:"",status,unit,value)
fun RemoteSensor.toRetrofitRemoteSensor(): RetrofitRemoteSensor = RetrofitRemoteSensor(uid,centralUid,name,address,imageUrl,status,unit,value)