package com.example.dangmyodang.sido

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/sido")
    fun getInfo(
        @Query("pageNo")pageNo:String,
        @Query("numOfRows")numOfRows:String,
        @Query("_type")type:String,
        @Query("serviceKey")serviceKey:String = "l+S2yIYFVqM4CiiVEfmlyaGd7wmWk3KOnhtCVast8Ocwlfz1oymc5OEqNnurYI8g2+lWNCbS7Ii2ht6UTWpPOw=="
    ): Call<Sido>
}