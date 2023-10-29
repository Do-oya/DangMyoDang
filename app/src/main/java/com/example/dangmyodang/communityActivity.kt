package com.example.dangmyodang

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class communityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.vertical_exit)
        }
    }
}