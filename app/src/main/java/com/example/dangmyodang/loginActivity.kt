package com.example.dangmyodang

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class loginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val next = findViewById<Button>(R.id.next)
        next.setOnClickListener { // 캘린더
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}