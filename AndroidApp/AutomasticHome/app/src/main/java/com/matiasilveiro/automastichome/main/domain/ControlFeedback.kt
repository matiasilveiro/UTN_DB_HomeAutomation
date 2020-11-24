package com.matiasilveiro.automastichome.main.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ControlFeedback(
        var uid: String,
        var name: String,
        var referenceValue: Int,
        var actionTrue: Int,
        var actionFalse: Int,
        var condition: String
) : Parcelable
