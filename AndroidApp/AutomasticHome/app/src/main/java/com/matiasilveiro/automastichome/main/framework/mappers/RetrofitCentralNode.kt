package com.matiasilveiro.automastichome.main.framework.mappers

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.matiasilveiro.automastichome.main.domain.CentralNode

@Keep
class RetrofitCentralNode(
    @SerializedName("NodeId") val uid: String,
    @SerializedName("Name") val name: String,
    @SerializedName("Address") val address: String,
    @SerializedName("Password") val password: String,
    @SerializedName("ImageUrl") val imageUrl: String?,
    @SerializedName("Role") var role: Int)

fun RetrofitCentralNode.toCentralNode(): CentralNode = CentralNode(uid,name,address,password,imageUrl?:"",role)
fun CentralNode.toRetrofitCentralNode(): RetrofitCentralNode = RetrofitCentralNode(uid,name,address,password,imageUrl,role)