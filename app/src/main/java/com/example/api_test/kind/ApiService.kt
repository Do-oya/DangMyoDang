package com.example.api_test.kind

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService_kind {
    @GET("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/kind")
    fun getInfo(
        @Query("up_kind_cd")up_kind_cd:String,
        @Query("_type")type:String,
        @Query("serviceKey")serviceKey:String = "l+S2yIYFVqM4CiiVEfmlyaGd7wmWk3KOnhtCVast8Ocwlfz1oymc5OEqNnurYI8g2+lWNCbS7Ii2ht6UTWpPOw=="
    ): Call<Kind>
}