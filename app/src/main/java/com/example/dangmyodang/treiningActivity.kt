package com.example.dangmyodang

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class treiningActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treining)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.vertical_exit)
        }
    }
}