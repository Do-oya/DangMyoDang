package com.example.dangmyodang

import android.content.Intent
import android.os.Bundle
import android.widget.Button

class careActivity : BaseActivity(TransitionMode.HORIZON) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_care)

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