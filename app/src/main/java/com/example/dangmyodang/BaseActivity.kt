package com.example.dangmyodang

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(
    private val transitionMode: TransitionMode = TransitionMode.NONE
) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInject(savedInstanceState)

        when (transitionMode) {
            TransitionMode.HORIZON -> overridePendingTransition(R.anim.horizon_enter, R.anim.none)
            TransitionMode.VERTICAL -> overridePendingTransition(R.anim.vertical_enter, R.anim.none)
            else -> Unit
        }
    }

    override fun finish() {
        super.finish()

        when (transitionMode) {
            TransitionMode.HORIZON -> overridePendingTransition(R.anim.none, R.anim.horizon_exit)
            TransitionMode.VERTICAL -> overridePendingTransition(R.anim.none, R.anim.vertical_exit)
            else -> Unit
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            when (transitionMode) {
                TransitionMode.HORIZON -> overridePendingTransition(R.anim.none, R.anim.horizon_exit)
                TransitionMode.VERTICAL -> overridePendingTransition(R.anim.none, R.anim.vertical_exit)
                else -> Unit
            }
        }
    }

    enum class TransitionMode {
        NONE,
        HORIZON,
        VERTICAL
    }

    open fun onInject(savedInstanceState: Bundle?) {
        // Subclasses can override this method to perform any initialization
    }
}