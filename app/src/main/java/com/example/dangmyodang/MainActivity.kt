package com.example.dangmyodang

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dangmyodang.spinnerActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity(TransitionMode.HORIZON) {

    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.textView5).isSelected = true
        textView = findViewById(R.id.textView)
        val intent = intent
        val username = intent.getStringExtra("userName")
        textView.text = "$username 님"

        // 버튼을 찾아옴
        val calendar = findViewById<ImageButton>(R.id.calendar)
        val map = findViewById<ImageButton>(R.id.map)
        val training = findViewById<ImageButton>(R.id.training)
        val a = findViewById<ImageButton>(R.id.a)
        val community = findViewById<ImageButton>(R.id.community)
        val running = findViewById<ImageButton>(R.id.running)
        val care = findViewById<ImageButton>(R.id.care)

        // BottomNavigationView 초기화
        bottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    if (item.isChecked) {
                        Log.d("NavigationView", "Already checked: Home Fragment")
                        return@setOnNavigationItemSelectedListener false
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.careFragment -> {
                    if (item.isChecked) {
                        Log.d("NavigationView", "Already checked: care Fragment")
                        return@setOnNavigationItemSelectedListener false
                    }
                    startActivity(Intent(this, showActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // 기존 버튼 클릭 리스너를 설정
        calendar.setOnClickListener {
            val intent = Intent(this, calendarActivity::class.java)
            val userName = "댕묘댕"
            intent.putExtra("userName", userName)
            startActivity(intent)
        }
        map.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
        training.setOnClickListener {
            val intent = Intent(this, treiningActivity::class.java)
            startActivity(intent)
        }
        a.setOnClickListener {
            val intent = Intent(this, spinnerActivity::class.java)
            startActivity(intent)
        }
        community.setOnClickListener {
            val intent = Intent(this, communityActivity::class.java)
            startActivity(intent)
        }
        running.setOnClickListener {
            val intent = Intent(this, RunningActivity::class.java)
            startActivity(intent)
        }
        care.setOnClickListener {
            val intent = Intent(this, careActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrameLayout, fragment)
            .commit()
    }
}
