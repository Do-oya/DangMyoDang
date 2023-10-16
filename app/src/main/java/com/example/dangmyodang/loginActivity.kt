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

        val login = findViewById<Button>(R.id.login)
        val join = findViewById<Button>(R.id.join)
        login.setOnClickListener { // 로그인
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        join.setOnClickListener { // 회원가입
            val intent = Intent(this, joinActivity::class.java)
            startActivity(intent)
        }
    }
}