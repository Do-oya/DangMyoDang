package com.example.dangmyodang.sido

import com.google.gson.annotations.SerializedName

data class Sido(
    @SerializedName("response") val response: Response
)

data class Response(
    @SerializedName("header") val header: Header,
    @SerializedName("body") val body: Body
)

data class Header(
    @SerializedName("reqNo") val reqNo: Int,
    @SerializedName("resultCode") val resultCode: String,
    @SerializedName("resultMsg") val resultMsg: String
)

data class Body(
    @SerializedName("items") val items: Items,
    @SerializedName("numOfRows") val numOfRows: Int,
    @SerializedName("pageNo") val pageNo: Int,
    @SerializedName("totalCount") val totalCount: Int
)

data class Items(
    @SerializedName("item") val item: List<AdaptionList>
)

data class AdaptionList(
    @SerializedName("orgCd") val orgCd: String,
    @SerializedName("orgdownNm") val orgdownNm: String
) {
    override fun toString(): String{
        return "\ncom.example.api_test.AdaptionList : [\n" +
                "    orgCd : ${orgCd}\n" +
                "    orgdownNm : ${orgdownNm}]\n\n"
    }
}


