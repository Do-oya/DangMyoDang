package com.example.api_test.shelter

import com.google.gson.annotations.SerializedName

data class Shelter(
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
    @SerializedName("careRegNo") val careRegNo: String,
    @SerializedName("careNm") val careNm: String
) {
    override fun toString(): String{
        return "\nShelter : [\n" +
                "    careRegNo : ${careRegNo}\n" +
                "    careNm : ${careNm}]\n\n"
    }
}


