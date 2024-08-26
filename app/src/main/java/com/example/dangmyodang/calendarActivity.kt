package com.example.dangmyodang

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Calendar

class calendarActivity : BaseActivity(TransitionMode.HORIZON) {

    private var fileName: String = ""
    private var diaryText: String? = null
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var calendarView: CalendarView
    private lateinit var changeButton: Button
    private lateinit var deleteButton: Button
    private lateinit var saveButton: Button
    private lateinit var diaryTextView: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var contextEditText: EditText
    private lateinit var alarmButton: ImageButton

    private val MY_PERMISSIONS_REQUEST_VIBRATE = 123

    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDayOfMonth = 0
    private var selectedHour = 0
    private var selectedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // BottomNavigationView 초기화
        bottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    if (item.isChecked) {
                        Log.d("NavigationView", "Already checked: Home Fragment")
                        return@setOnNavigationItemSelectedListener false
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.careFragment -> {
                    if (item.isChecked) {
                        Log.d("NavigationView", "Already checked: care Fragment")
                        return@setOnNavigationItemSelectedListener false
                    }
                    startActivity(Intent(this, showActivity::class.java))
                    true
                }
                R.id.userFragement -> {
                    if (item.isChecked) {
                        Log.d("NavigationView", "Already checked: care Fragment")
                        return@setOnNavigationItemSelectedListener false
                    }
                    startActivity(Intent(this, settingActivity::class.java))
                    true
                }
                else -> false
            }
        }

        initializeViews()

        val intent = intent
        val username = intent.getStringExtra("userName")
        val userID = intent.getStringExtra("userID")
        textView3.text = "$username 님의 일정기록"

        alarmButton.setOnClickListener {
            // 사용자가 시간을 선택할 수 있도록 TimePickerDialog를 표시합니다.
            val calendar = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    // 사용자가 선택한 시간을 변수에 저장
                    selectedHour = hourOfDay
                    selectedMinute = minute
                    // 알림을 설정합니다.
                    setNotification(selectedYear, selectedMonth, selectedDayOfMonth, selectedHour, selectedMinute)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )

            timePickerDialog.show()
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedYear = year
            selectedMonth = month
            selectedDayOfMonth = dayOfMonth

            diaryTextView.visibility = View.VISIBLE
            saveButton.visibility = View.VISIBLE
            contextEditText.visibility = View.VISIBLE
            textView2.visibility = View.INVISIBLE
            changeButton.visibility = View.INVISIBLE
            deleteButton.visibility = View.INVISIBLE

            diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            contextEditText.text = null

            checkDay(year, month, dayOfMonth, userID)
        }

        saveButton.setOnClickListener {
            saveDiary(fileName)
            diaryText = contextEditText.text.toString()
            textView2.text = diaryText
            saveButton.visibility = View.INVISIBLE
            changeButton.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE
            contextEditText.visibility = View.INVISIBLE
            textView2.visibility = View.VISIBLE

            // 저장 버튼을 눌렀을 때만 알림 설정
            setNotification(selectedYear, selectedMonth, selectedDayOfMonth, selectedHour, selectedMinute)
        }
    }

    private fun initializeViews() {
        calendarView = findViewById(R.id.calendarView)
        diaryTextView = findViewById(R.id.diaryTextView)
        saveButton = findViewById(R.id.save_Btn)
        deleteButton = findViewById(R.id.del_Btn)
        changeButton = findViewById(R.id.cha_Btn)
        textView2 = findViewById(R.id.textView2)
        textView3 = findViewById(R.id.textView3)
        contextEditText = findViewById(R.id.contextEditText)
        alarmButton = findViewById(R.id.alram_Btn)
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                selectedHour = hourOfDay
                selectedMinute = minute
                setNotification(selectedYear, selectedMonth, selectedDayOfMonth, selectedHour, selectedMinute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun checkDay(cYear: Int, cMonth: Int, cDay: Int, userID: String?) {
        fileName = "$userID$cYear-${cMonth + 1}-$cDay.txt"
        val fis: FileInputStream?
        try {
            fis = openFileInput(fileName)
            val fileData = ByteArray(fis.available())
            fis.read(fileData)
            fis.close()
            diaryText = String(fileData)

            contextEditText.visibility = View.INVISIBLE
            textView2.visibility = View.VISIBLE
            textView2.text = diaryText

            saveButton.visibility = View.INVISIBLE
            changeButton.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE

            changeButton.setOnClickListener {
                contextEditText.visibility = View.VISIBLE
                textView2.visibility = View.INVISIBLE
                contextEditText.setText(diaryText)

                saveButton.visibility = View.VISIBLE
                changeButton.visibility = View.INVISIBLE
                deleteButton.visibility = View.INVISIBLE
                textView2.text = contextEditText.text

                // 날짜에 따라 알림 설정
                setNotification(selectedYear, selectedMonth, selectedDayOfMonth, selectedHour, selectedMinute)
            }

            deleteButton.setOnClickListener {
                textView2.visibility = View.INVISIBLE
                contextEditText.text = null
                contextEditText.visibility = View.VISIBLE
                saveButton.visibility = View.VISIBLE
                changeButton.visibility = View.INVISIBLE
                deleteButton.visibility = View.INVISIBLE

                removeDiary(fileName)
                cancelNotification()
            }

            if (textView2.text == null) {
                textView2.visibility = View.INVISIBLE
                diaryTextView.visibility = View.VISIBLE
                saveButton.visibility = View.VISIBLE
                changeButton.visibility = View.INVISIBLE
                deleteButton.visibility = View.INVISIBLE
                contextEditText.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setNotification(year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) {
        Log.d("MyApp", "알림 설정 시도: $year-${month + 1}-$dayOfMonth $hourOfDay:$minute")

        val channelId = "channel_id"
        val channelName = "channel_name"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val userEnteredContent = contextEditText.text.toString()

        // 알림이 생성될 때마다 정보를 SharedPreferences에 저장
        saveNotificationInfo(year, month, dayOfMonth, hourOfDay, minute, userEnteredContent)

        val requestCode = (calendar.timeInMillis % Int.MAX_VALUE).toInt() // 고유한 requestCode 생성

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("notificationContent", userEnteredContent)
        intent.putExtra("year", year)
        intent.putExtra("month", month)
        intent.putExtra("dayOfMonth", dayOfMonth)
        intent.putExtra("hourOfDay", hourOfDay)
        intent.putExtra("minute", minute)

        val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        textView3.text = String.format("알람이 %02d:%02d에 설정되었습니다.", hourOfDay, minute)
        Log.d("MyApp", "알람이 설정되었습니다. 시간: $hourOfDay:$minute")
    }

    private fun saveNotificationInfo(year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int, content: String) {
        val sharedPreferences = getSharedPreferences("NotificationInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("year", year)
        editor.putInt("month", month)
        editor.putInt("dayOfMonth", dayOfMonth)
        editor.putInt("hourOfDay", hourOfDay)
        editor.putInt("minute", minute)
        editor.putString("content", content)

        editor.apply()
    }

    internal fun showNotification(title: String, content: String?) {
        Log.d("MyApp", "알림 표시 시도: $title - $content")

        val channelId = "channel_id"
        val channelName = "channel_name"
        val notificationId = 1

        // 알림 채널 생성 (Oreo 이상에서 필요)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ma)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // 알림 표시
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
            Log.d("MyApp", "알림이 표시되었습니다. 내용: $content")
        }
    }
    private fun showToastOnUiThread(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancelNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.cancel(pendingIntent)
    }




    private fun isAppInForeground(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses ?: return false

        for (processInfo in runningAppProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (processInfo.processName == packageName) {
                    return true
                }
            }
        }
        return false
    }

    private fun showToast(message: String) {
        showToastOnUiThread(message)
    }

    private fun saveDiary(readDay: String) {
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(readDay, Context.MODE_PRIVATE)
            val content = contextEditText.text.toString()
            fos.write(content.toByteArray())
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun removeDiary(readDay: String) {
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(readDay, Context.MODE_PRIVATE)
            val content = ""
            fos.write(content.toByteArray())
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationContent = intent.getStringExtra("notificationContent")
        val year = intent.getIntExtra("year", 0)
        val month = intent.getIntExtra("month", 0)
        val dayOfMonth = intent.getIntExtra("dayOfMonth", 0)
        val hourOfDay = intent.getIntExtra("hourOfDay", 0)
        val minute = intent.getIntExtra("minute", 0)

        Log.d("MyApp", "알림 수신: $notificationContent")

        // showNotification 함수 호출
        showNotification(context, "일정기록 알림", notificationContent)

        val channelId = "channel_id"
        val channelName = "channel_name"
        val notificationId = 1

        // 알림 채널 생성 (Oreo 이상에서 필요)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = context.applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.danglogo)
            .setContentTitle("일정기록 알림")
            .setContentText(notificationContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // 알림 표시
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }

    private fun showNotification(context: Context, title: String, content: String?) {
        Log.d("MyApp", "알림 표시 시도: $title - $content")

        val channelId = "channel_id"
        val channelName = "channel_name"
        val notificationId = 1

        // 알림 채널 생성 (Oreo 이상에서 필요)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ma)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // 알림 표시
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
            Log.d("MyApp", "알림이 표시되었습니다. 내용: $content")
        }
    }
}