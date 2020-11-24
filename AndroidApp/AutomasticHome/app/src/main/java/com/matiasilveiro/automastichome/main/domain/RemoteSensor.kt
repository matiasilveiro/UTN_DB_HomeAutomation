package com.matiasilveiro.automastichome.main.domain

data class RemoteSensor(
        var uid: String,
        var centralUid: String,
        var name: String,
        var address: String,
        var status: String,
        var unit: String,
        var value: Int)
    : RemoteNode(uid,centralUid,name,address,status)

