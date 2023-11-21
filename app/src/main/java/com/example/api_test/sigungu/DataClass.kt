package com.example.api_test.sigungu

import com.google.gson.annotations.SerializedName

data class Sigungu(
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
    @SerializedName("items") val items: Items
)

data class Items(
    @SerializedName("item") val item: List<AdaptionList>
)

data class AdaptionList(
    @SerializedName("uprCd") val uprCd: String,
    @SerializedName("orgCd") val orgCd: String,
    @SerializedName("orgdownNm") val orgdownNm: String
) {
    override fun toString(): String{
        return "\nSigungu : [\n" +
                "    uprCd : ${orgCd}\n" +
                "    orgCd : ${orgCd}\n" +
                "    orgdownNm : ${orgdownNm}]\n\n"
    }
}


