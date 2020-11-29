package com.matiasilveiro.automastichome.main.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CentralNode(var uid: String, var name: String, var address: String, var password: String, var imageUrl: String, var role: Int): Parcelable
