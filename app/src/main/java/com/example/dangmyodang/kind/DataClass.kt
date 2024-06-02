package com.example.dangmyodang.kind

import com.google.gson.annotations.SerializedName

data class Kind(
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
    @SerializedName("kindCd") val kindCd: String,
    @SerializedName("KNm") val KNm: String
) {
    override fun toString(): String{
        return "\nKind : [\n" +
                "    kindCd : ${kindCd}\n" +
                "    KNm : ${KNm}]\n\n"
    }
}


