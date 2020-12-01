package com.matiasilveiro.automastichome.main.framework.mappers

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.matiasilveiro.automastichome.main.domain.ControlFeedback

@Keep
class RetrofitControlFeedback(
        @SerializedName("ActionId") val uid: String,
        @SerializedName("Name") val name: String,
        @SerializedName("ReferenceValue") val referenceValue: Int,
        @SerializedName("ActionTrue") val actionTrue: Int,
        @SerializedName("ActionFalse") val actionFalse: Int,
        @SerializedName("Condition") val condition: String)

fun RetrofitControlFeedback.toControlFeedback(): ControlFeedback = ControlFeedback(uid,name,referenceValue,actionTrue,actionFalse,condition)
fun ControlFeedback.toRetrofitControlFeedback(): RetrofitControlFeedback = RetrofitControlFeedback(uid,name,referenceValue,actionTrue,actionFalse,condition)