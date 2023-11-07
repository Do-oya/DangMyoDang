package com.example.dangmyodang

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : BaseActivity(TransitionMode.VERTICAL) {
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
        val care = findViewById<ImageButton>(R.id.care)

        // 버튼 클릭 리스너를 설정
        calendar.setOnClickListener { // 캘린더
            val intent = Intent(this, calendarActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
        map.setOnClickListener { // 맵
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
        training.setOnClickListener { // 훈련
            val intent = Intent(this, treiningActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
        a.setOnClickListener { // 입양
            val intent = Intent(this, aActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
        community.setOnClickListener { // 커뮤니티
            val intent = Intent(this, communityActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
        running.setOnClickListener { // 산책기록
            val intent = Intent(this, runningActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
        care.setOnClickListener { // 건강정보
            val intent = Intent(this, careActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.vertical_exit)
        }
    }
}