package com.example.dangmyodang

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView

class careActivity : BaseActivity(TransitionMode.HORIZON) {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_care)

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
                R.id.userFragement -> {
                    if (item.isChecked) {
                        Log.d("NavigationView", "Already checked: care Fragment")
                        return@setOnNavigationItemSelectedListener false
                    }
                    startActivity(Intent(this, settingActivity::class.java))
                    true
                }
                else -> false
            }
        }

        val food = findViewById<Button>(R.id.food)
        val disease = findViewById<Button>(R.id.disease)
        val vaccination = findViewById<Button>(R.id.vaccination)
        val medic = findViewById<Button>(R.id.medic)

        food.setOnClickListener { // 먹으면 안되는 음식
            val intent = Intent(this, foodActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
        disease.setOnClickListener {  // 질병
            val intent = Intent(this, diseaseActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
        vaccination.setOnClickListener { // 예방접종
            val intent = Intent(this, vaccinationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
        medic.setOnClickListener { // 응급처치
            val intent = Intent(this, medicActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}