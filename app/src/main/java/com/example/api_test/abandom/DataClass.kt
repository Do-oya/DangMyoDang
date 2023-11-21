package com.example.api_test.abandom

import com.google.gson.annotations.SerializedName

data class Abandom(
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
    //@SerializedName("desertionNo") val desertionNo: String,
    //@SerializedName("filename") val filename: String, //Thumbnail Image
    @SerializedName("happenPlace") val happenPlace: String,
    @SerializedName("kindCd") val kindCd: String,
    //@SerializedName("colorCd") val colorCd: String,
    @SerializedName("age") val age: String,
    @SerializedName("weight") val weight: String,
    //@SerializedName("noticeNo") val noticeNo: String,
    //@SerializedName("noticeSdt") val noticeSdt: String,
   // @SerializedName("noticeEdt") val noticeEdt: String,
    @SerializedName("popfile") val popfile: String, //Image
    //@SerializedName("processState") val processState: String,
   // @SerializedName("sexCd") val sexCd: String,
   // @SerializedName(",") val neuterYn: String,
    @SerializedName("specialMark") val specialMark: String,
   // @SerializedName("careNm") val careNm: String,
   // @SerializedName("careTel") val careTel: String,
    @SerializedName("careAddr") val careAddr: String,
   // @SerializedName("orgNm") val orgNm: String,
   // @SerializedName("chargeNm") val chargeNm: String,
   // @SerializedName("officetel") val officetel: String,
   // @SerializedName("noticeComment") val noticeComment: String,
) {
    override fun toString(): String{
        return "\nAbandom : [\n" +
//               "    desertionNo : ${desertionNo}\n" +
//                "    filename : ${filename}\n" +
                "    happenPlace : ${happenPlace}\n" +
//                "    kindCd : ${kindCd}\n" +
//                "    colorCd : ${colorCd}\n" +
                "    age : ${age}\n" +
//                "    weight : ${weight}\n" +
//               "    noticeNo : ${noticeNo}\n" +
//               "    noticeSdt : ${noticeSdt}\n" +
//               "    noticeEdt : ${noticeEdt}\n" +
                "    popfile : ${popfile}\n"
//                "    processState : ${processState}\n" +
//                "    sexCd : ${sexCd}\n" +
//                "    neuterYn : ${neuterYn}\n" +
//                "    specialMark : ${specialMark}\n" +
//                "    careNm : ${careNm}\n" +
//                "    careTel : ${careTel}\n" +
//                "    orgNm : ${orgNm}\n" +
//                "    chargeNm : ${chargeNm}\n" +
//                "    officetel : ${officetel}\n" +
//                "    careAddr : ${careAddr}\n" +
//                "    noticeComment : ${noticeComment}]\n\n"
    }
}


