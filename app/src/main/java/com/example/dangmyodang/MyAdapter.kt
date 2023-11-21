package com.example.dangmyodang

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.api_test.AdaptionList
import org.json.JSONException
import org.json.JSONObject

class MyAdapter(private val context: Context, private val resource: Int, private val isRecyclerView: Boolean = false, private val dataList: List<Any>) :
    ArrayAdapter<Any>(context, resource, dataList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (isRecyclerView) {
            val viewHolder: ViewHolder
            val view: View

            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(resource, parent, false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            } else {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }

            val item = dataList[position] as AdaptionList

            viewHolder.happenPalce.text = item.happenPlace
            viewHolder.kindCd.text = item.kindCd
            viewHolder.age.text = item.age
            viewHolder.weight.text = item.weight
            viewHolder.careAddr.text = item.careAddr

            Glide.with(context)
                .load(item.popfile)
                .into(viewHolder.imageView2)

            return view
        } else {
            val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
            val item = getItem(position) as JSONObject

            val holder = ViewHolder(view)
            holder.bindData(item)

            return view
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val happenPalce: TextView = itemView.findViewById(R.id.happenPalce)
        val kindCd: TextView = itemView.findViewById(R.id.kindCd)
        val age: TextView = itemView.findViewById(R.id.age)
        val weight: TextView = itemView.findViewById(R.id.weight)
        val careAddr: TextView = itemView.findViewById(R.id.careAddr)
        val imageView2: ImageView = itemView.findViewById(R.id.imageView2)

        fun bindData(item: JSONObject) {
            try {
                val text = item.getString("key")
                happenPalce.text = text
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}
