package com.example.dangmyodang

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.dangmyodang.AdaptionList

class MyAdapter(context: Context, resource: Int, objects: List<AdaptionList?>) :
    ArrayAdapter<AdaptionList>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)

        val item = getItem(position)

        // item 레이아웃 정의
        val itemLayout = LinearLayout(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // 여백 조절
        layoutParams.setMargins(10, 10, 10, 10) // 여백 값은 상황에 따라 조절해주세요.

        // item 레이아웃에 여백 적용
        itemLayout.layoutParams = layoutParams

        val happenPalce = view.findViewById<TextView>(R.id.happenPalce)
        val kindCd = view.findViewById<TextView>(R.id.kindCd)
        val age = view.findViewById<TextView>(R.id.age)
        val weight = view.findViewById<TextView>(R.id.weight)
        //val specialmark = view.findViewById<TextView>(R.id.specialmark)
        val careAddr = view.findViewById<TextView>(R.id.careAddr)
        val imageView2 = view.findViewById<ImageView>(R.id.imageView2)

        happenPalce.text = item?.happenPlace
        kindCd.text = item?.kindCd
        age.text = item?.age
        weight.text = item?.weight
        //specialmark.text = item?.specialmark
        careAddr.text = item?.careAddr
        Glide.with(context)
            .load(item?.popfile)
            .into(imageView2)

        return view
    }
}