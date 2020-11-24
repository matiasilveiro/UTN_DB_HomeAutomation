package com.matiasilveiro.automastichome.main.framework.mappers

import com.matiasilveiro.automastichome.main.domain.RemoteSensor

class FirebaseRemoteSensor(
        val uid: String,
        val centralUid: String,
        val name: String,
        val address: String,
        val imageUrl: String,
        val status: String,
        val unit: String,
        val value: Int)
    : FirebaseRemoteNode(uid,centralUid,name,address,status)
{
    constructor() : this("","","","","","","",0)
}

fun FirebaseRemoteSensor.toRemoteSensor(): RemoteSensor = RemoteSensor(uid,centralUid,name,address,imageUrl,status,unit,value)
fun RemoteSensor.toFirebaseRemoteSensor(): FirebaseRemoteSensor = FirebaseRemoteSensor(uid,centralUid,name,address,imageUrl,status,unit,value)