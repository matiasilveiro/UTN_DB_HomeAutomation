package com.matiasilveiro.automastichome.main.framework.mappers

import com.matiasilveiro.automastichome.main.domain.RemoteActuator

class FirebaseRemoteActuator(
        val uid: String,
        val centralUid: String,
        val name: String,
        val address: String,
        val imageUrl: String,
        val status: String,
        val type: String,
        val value: Int)
    : FirebaseRemoteNode(uid,centralUid,name,address,status)
{
    constructor() : this("","","","","","","",0)
}

fun FirebaseRemoteActuator.toRemoteActuator(): RemoteActuator = RemoteActuator(uid,centralUid,name,address,imageUrl,status,type,value)
fun RemoteActuator.toFirebaseRemoteActuator(): FirebaseRemoteActuator = FirebaseRemoteActuator(uid,centralUid,name,address,imageUrl,status,type,value)