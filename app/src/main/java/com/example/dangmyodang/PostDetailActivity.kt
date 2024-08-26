package com.example.dangmyodang

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PostDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        // 인텐트에서 데이터를 가져옴
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")

        // 레이아웃의 TextView에 데이터를 설정
        val textViewTitle: TextView = findViewById(R.id.textViewTitle)
        val textViewContent: TextView = findViewById(R.id.textViewContent)

        textViewTitle.text = title
        textViewContent.text = content
    }
}