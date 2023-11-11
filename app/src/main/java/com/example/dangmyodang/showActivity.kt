package com.example.dangmyodang

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class showActivity : BaseActivity(TransitionMode.HORIZON) {

    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        val btnCall: Button = findViewById(R.id.saryo)
        val btnThreeIdiotsCoding: Button = findViewById(R.id.young)

        // BottomNavigationView 초기화
        bottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.careFragment -> {
                    val intent = Intent(this, showActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        btnCall.setOnClickListener { onClick(it) }
        btnThreeIdiotsCoding.setOnClickListener { onClick(it) }
    }

    fun onClick(v: View) {
        val intent = Intent(Intent.ACTION_VIEW)
        when (v.id) {
            R.id.saryo -> {
                intent.data = Uri.parse("https://m.pet-friends.co.kr/main/tab/2")
                startActivity(intent)
            }
            R.id.young -> {
                intent.data = Uri.parse("https://puppydog.co.kr/")
                startActivity(intent)
            }
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