package com.matiasilveiro.automastichome.main.ui.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RemoteSensorUI (var uid: String, var name: String, var imageUrl: String) : Parcelable {
}