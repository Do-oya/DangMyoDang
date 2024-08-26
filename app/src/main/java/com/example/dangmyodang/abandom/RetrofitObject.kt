package com.example.dangmyodang.abandom

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var gson= GsonBuilder().setLenient().create()
val clientBuilder = OkHttpClient.Builder()
val loggingInterceptor = HttpLoggingInterceptor()

object RetrofitObject_abandom {

    private fun getRetrofit_abandom(): Retrofit {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)
        return Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(clientBuilder.build())
            .build()
    }
    fun getApiService_abandom(): ApiService_abandom {
        return getRetrofit_abandom().create(ApiService_abandom::class.java)
    }
}
