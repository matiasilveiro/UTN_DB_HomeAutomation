package com.matiasilveiro.automastichome.main.framework.mappers

import com.google.gson.annotations.SerializedName

class RetrofitResult(
        @SerializedName("status") val status: String,
        @SerializedName("message") val message: String) {
}