package com.example.dangmyodang

import android.content.Intent
import android.os.Bundle
import android.widget.Button

class treiningActivity : BaseActivity(TransitionMode.HORIZON) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treining)

        val dog_tr = findViewById<Button>(R.id.dog_tr)
        val dog_hand = findViewById<Button>(R.id.dog_hand)
        val dog_bing = findViewById<Button>(R.id.dog_bing)
        val dog_push = findViewById<Button>(R.id.dog_push)
        val dog_com = findViewById<Button>(R.id.dog_com)
        val dog_comone = findViewById<Button>(R.id.dog_comone)

        dog_tr.setOnClickListener { // 기다려
            val intent = Intent(this, dog_trActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
        dog_hand.setOnClickListener {  // 손
            val intent = Intent(this, dog_handActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
        dog_bing.setOnClickListener { // 돌아
            val intent = Intent(this, dog_bingActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
        dog_push.setOnClickListener { // 엎드려
            val intent = Intent(this, dog_pushActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
        dog_com.setOnClickListener { // 가져와
            val intent = Intent(this, dog_comActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }
        dog_comone.setOnClickListener { // 이리와
            val intent = Intent(this, dog_comoneActivity::class.java)
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