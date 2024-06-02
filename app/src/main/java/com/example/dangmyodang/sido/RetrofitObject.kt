package com.example.dangmyodang.sido

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var gson= GsonBuilder().setLenient().create()
val clientBuilder = OkHttpClient.Builder()
val loggingInterceptor = HttpLoggingInterceptor()

object RetrofitObject {

    private fun getRetrofit(): Retrofit {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)
        return Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(clientBuilder.build())
            .build()
    }
    fun getApiService(): ApiService {
        return getRetrofit().create(ApiService::class.java)
    }
}
