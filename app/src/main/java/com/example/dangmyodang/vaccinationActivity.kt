package com.example.dangmyodang

import android.os.Bundle

class vaccinationActivity : BaseActivity(TransitionMode.VERTICAL) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccination)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.vertical_exit)
        }
    }
}