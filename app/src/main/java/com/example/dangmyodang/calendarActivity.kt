package com.example.dangmyodang

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
//import com.example.dangmyodang.databinding.ActivityCalendarBinding  // 뷰 바인딩 클래스를 import

class calendarActivity : AppCompatActivity() {
//    private val CHANNEL_ID = "my_notification_channel"
//    private lateinit var binding: ActivityCalendarBinding  // 뷰 바인딩 인스턴스
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_calendar)
//
//        createNotificationChannel()
//
//        binding.notifyButton.setOnClickListener {
//            val inputText = binding.dataInput.text.toString()
//            if (inputText.isNotEmpty()) {
//                val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setSmallIcon(R.drawable.aaa)
//                    .setContentTitle("일정 알림")
//                    .setContentText(inputText)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//                with(NotificationManagerCompat.from(this)) {
//                    val notification: Notification = builder.build()
//                    notify(1, notification)
//                }
//            }
//        }
//    }
//
//    private fun createNotificationChannel() {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            val name = "My Notification Channel"
//            val descriptionText = "My notification channel description"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//                description = descriptionText
//            }
//
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
    }
}
