package com.example.dangmyodang.abandom

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService_abandom {
    @GET("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic")
    fun getInfo(
        @Query("bgnde")bgnde:String,
        @Query("endde")endde:String,
        @Query("upkind")upkind:String,
        //@Query("kind")kind:String,
        @Query("upr_cd")upr_cd:String,
        @Query("org_cd")org_cd:String,
        //@Query("care_reg_no")care_reg_no:String,
        //@Query("state")state:String,
        @Query("neuter_yn")neuter_yn:String,
        @Query("pageNo")pageNo:String,
        @Query("numOfRows")numOfRows:String,
        @Query("_type")type:String,
        @Query("serviceKey")serviceKey:String = "l+S2yIYFVqM4CiiVEfmlyaGd7wmWk3KOnhtCVast8Ocwlfz1oymc5OEqNnurYI8g2+lWNCbS7Ii2ht6UTWpPOw=="
    ): Call<Abandom>
}