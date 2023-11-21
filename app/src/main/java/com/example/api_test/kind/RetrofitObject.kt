package com.example.api_test.kind

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var gson= GsonBuilder().setLenient().create()
val clientBuilder = OkHttpClient.Builder()
val loggingInterceptor = HttpLoggingInterceptor()

object RetrofitObject_kind {

    private fun getRetrofit_kind(): Retrofit {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)
        return Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(clientBuilder.build())
            .build()
    }
    fun getApiService_kind(): ApiService_kind {
        return getRetrofit_kind().create(ApiService_kind::class.java)
    }
}
