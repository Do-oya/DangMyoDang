package com.example.dangmyodang

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import java.io.FileInputStream
import java.io.FileOutputStream

class calendarActivity : BaseActivity(TransitionMode.HORIZON) {

    var fname: String = "" // 파일 이름 초기화
    var str: String? = null
    lateinit var calendarView: CalendarView
    lateinit var cha_Btn: Button
    lateinit var del_Btn: Button
    lateinit var save_Btn: Button
    lateinit var diaryTextView: TextView
    lateinit var textView2: TextView
    lateinit var textView3: TextView
    lateinit var contextEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        calendarView = findViewById(R.id.calendarView)
        diaryTextView = findViewById(R.id.diaryTextView)
        save_Btn = findViewById(R.id.save_Btn)
        del_Btn = findViewById(R.id.del_Btn)
        cha_Btn = findViewById(R.id.cha_Btn)
        textView2 = findViewById(R.id.textView2)
        textView3 = findViewById(R.id.textView3)
        contextEditText = findViewById(R.id.contextEditText)

        val intent = intent
        val username = intent.getStringExtra("userName")
        val userID = intent.getStringExtra("userID")
        textView3.text = "$username 님의 달력 일기장"

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            diaryTextView.visibility = View.VISIBLE
            save_Btn.visibility = View.VISIBLE
            contextEditText.visibility = View.VISIBLE
            textView2.visibility = View.INVISIBLE
            cha_Btn.visibility = View.INVISIBLE
            del_Btn.visibility = View.INVISIBLE
            diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            contextEditText.text = null
            checkDay(year, month, dayOfMonth, userID)
        }

        save_Btn.setOnClickListener {
            saveDiary(fname)
            str = contextEditText.text.toString()
            textView2.text = str
            save_Btn.visibility = View.INVISIBLE
            cha_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE
            contextEditText.visibility = View.INVISIBLE
            textView2.visibility = View.VISIBLE
        }
    }

    private fun checkDay(cYear: Int, cMonth: Int, cDay: Int, userID: String?) {
        fname = "$userID$cYear-${cMonth + 1}-$cDay.txt"
        val fis: FileInputStream?
        try {
            fis = openFileInput(fname)
            val fileData = ByteArray(fis.available())
            fis.read(fileData)
            fis.close()
            str = String(fileData)
            contextEditText.visibility = View.INVISIBLE
            textView2.visibility = View.VISIBLE
            textView2.text = str
            save_Btn.visibility = View.INVISIBLE
            cha_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE
            cha_Btn.setOnClickListener {
                contextEditText.visibility = View.VISIBLE
                textView2.visibility = View.INVISIBLE
                contextEditText.setText(str)
                save_Btn.visibility = View.VISIBLE
                cha_Btn.visibility = View.INVISIBLE
                del_Btn.visibility = View.INVISIBLE
                textView2.text = contextEditText.text
            }
            del_Btn.setOnClickListener {
                textView2.visibility = View.INVISIBLE
                contextEditText.text = null
                contextEditText.visibility = View.VISIBLE
                save_Btn.visibility = View.VISIBLE
                cha_Btn.visibility = View.INVISIBLE
                del_Btn.visibility = View.INVISIBLE
                removeDiary(fname)
            }
            if (textView2.text == null) {
                textView2.visibility = View.INVISIBLE
                diaryTextView.visibility = View.VISIBLE
                save_Btn.visibility = View.VISIBLE
                cha_Btn.visibility = View.INVISIBLE
                del_Btn.visibility = View.INVISIBLE
                contextEditText.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    private fun removeDiary(readDay: String) {
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            val content = ""
            fos.write(content.toByteArray())
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    private fun saveDiary(readDay: String) {
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            val content = contextEditText.text.toString()
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

