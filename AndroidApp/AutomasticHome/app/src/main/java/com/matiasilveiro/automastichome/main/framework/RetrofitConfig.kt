package com.matiasilveiro.automastichome.main.framework

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {
    val apiClient: ApiService = Retrofit.Builder()
        .baseUrl("https://192.168.56.1:5000")
        .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}