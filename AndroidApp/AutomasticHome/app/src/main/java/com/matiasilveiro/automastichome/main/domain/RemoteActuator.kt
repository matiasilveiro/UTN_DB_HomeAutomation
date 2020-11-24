package com.matiasilveiro.automastichome.main.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RemoteActuator(
        var uid: String,
        var centralUid: String,
        var name: String,
        var address: String,
        var imageUrl: String,
        var status: String,
        var type: String,
        var value: Int)
    : RemoteNode(uid,centralUid,name,address,status), Parcelable
