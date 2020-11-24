package com.matiasilveiro.automastichome.main.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RemoteSensor(
        var uid: String,
        var centralUid: String,
        var name: String,
        var address: String,
        var imageUrl: String,
        var status: String,
        var unit: String,
        var value: Int)
    : RemoteNode(uid,centralUid,name,address,status), Parcelable

