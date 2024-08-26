package com.example.dangmyodang

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class cal_page_Activity : BaseActivity(TransitionMode.HORIZON) {

    private lateinit var notificationTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cal_page)

        notificationTextView = findViewById(R.id.notificationTextView)

        // SharedPreferences에서 알림 내용 가져오기
        val notificationContent = getNotificationInfo()

        // 가져온 데이터를 표시
        notificationContent?.let {
            showNotificationContent(it)
            Log.d("MyApp", "onCreate - Notification Content: $it")
        }
    }

    override fun onResume() {
        super.onResume()

        // SharedPreferences에서 알림 내용 가져오기
        val notificationContent = getNotificationInfo()

        // 가져온 데이터를 표시
        notificationContent?.let {
            showNotificationContent(it)
            Log.d("MyApp", "onResume - Notification Content: $it")
        }
    }

    // SharedPreferences에서 알림 내용을 가져오는 함수
    private fun getNotificationInfo(): String? {
        val sharedPreferences = getSharedPreferences("NotificationInfo", Context.MODE_PRIVATE)
        // SharedPreferences에서 저장된 알림 내용을 가져옴
        return sharedPreferences.getString("content", null)
    }

    // 알림 내용을 화면에 표시하는 함수
    private fun showNotificationContent(notificationContent: String) {
        notificationTextView.text = notificationContent
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}
