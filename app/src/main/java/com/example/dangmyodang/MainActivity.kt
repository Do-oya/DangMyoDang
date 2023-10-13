package com.example.dangmyodang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent // Intent 클래스 import 추가
import android.widget.Button // 버튼을 사용하기 위한 import 추가
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 버튼을 찾아옴
        val calendar = findViewById<ImageButton>(R.id.calendar)
        val map = findViewById<ImageButton>(R.id.map)
        val training = findViewById<ImageButton>(R.id.training)
        val a = findViewById<ImageButton>(R.id.a)
        val community = findViewById<ImageButton>(R.id.community)
        val running = findViewById<ImageButton>(R.id.running)

        // 버튼 클릭 리스너를 설정
        calendar.setOnClickListener { // 캘린더
            val intent = Intent(this, calendarActivity::class.java)
            startActivity(intent)
        }
        map.setOnClickListener { // 맵
            val intent = Intent(this, mapActivity::class.java)
            startActivity(intent)
        }
        training.setOnClickListener { // 훈련
            val intent = Intent(this, treiningActivity::class.java)
            startActivity(intent)
        }
        a.setOnClickListener { // 입양
            val intent = Intent(this, aActivity::class.java)
            startActivity(intent)
        }
        community.setOnClickListener { // 커뮤니티
            val intent = Intent(this, communityActivity::class.java)
            startActivity(intent)
        }
        running.setOnClickListener { // 산책기록
            val intent = Intent(this, runningActivity::class.java)
            startActivity(intent)
        }
    }
}