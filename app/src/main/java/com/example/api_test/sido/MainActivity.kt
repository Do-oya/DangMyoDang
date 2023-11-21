package com.example.api_test.sido

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.api_test.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var buttonGet: Button
    private lateinit var textviewResponse: TextView

    private var findDate = SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.getDefault()).format(Date(System.currentTimeMillis()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonGet = findViewById(R.id.button_get)
        textviewResponse = findViewById(R.id.textview_response)

        setListener()

    }

    private fun setListener() {
        buttonGet.setOnClickListener {
            getAdaption()
        }
    }

    private fun getAdaption() {
        Log.d("print findDate", findDate)
        RetrofitObject.getApiService().getInfo("2", "3", "json").enqueue(object : Callback<Sido> {
            override fun onResponse(call: Call<Sido>, response: Response<Sido>) {
                setResponseText(response.code(), response.body())
                Toast.makeText(applicationContext, "success", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Sido>, t: Throwable) {
                Log.e("retrofit onFailure", t.message.toString())
                Toast.makeText(applicationContext, "fail", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setResponseText(resCode: Int, body: Sido?) {
        // 상태별 text 설정
        val responseText = when (resCode) {
            200 -> {
                if (body == null) {
                    "api body가 비어있습니다."
                } else {
                    body.toString()
                }
            }
            400 -> {
                "API 키가 만료되었거나 쿼리 파라미터가 잘못되었습니다."
            }
            401 -> {
                "인증 정보가 정확하지 않습니다."
            }
            500 -> {
                "API 서버에 문제가 발생했습니다."
            }
            else -> {
                "기타 문제 발생..."
            }
        }

        textviewResponse.text = responseText
    }
}