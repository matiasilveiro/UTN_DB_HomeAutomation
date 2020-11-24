package com.matiasilveiro.automastichome.main.domain

data class RemoteActuator(
        var uid: String,
        var centralUid: String,
        var name: String,
        var address: String,
        var status: String,
        var type: String,
        var value: Int)
    : RemoteNode(uid,centralUid,name,address,status)
