package com.example.dangmyodang

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

data class LoginData(
    val username: String,
    val password: String
)

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.id)
        val passwordEditText = findViewById<EditText>(R.id.pw)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000") // 실제 서버 URL로 변경
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val api = retrofit.create(MyApi::class.java)

        val loginButton = findViewById<Button>(R.id.login)
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val loginData = LoginData(username, password)

            val call = api.loginUser(loginData)

            call.enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse?.success == true) {
                            // 로그인 성공 메시지 표시
                            Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
                            // 로그인 성공 시 다음 화면으로 이동
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // 로그인 실패 메시지 표시
                            Toast.makeText(this@LoginActivity, "로그인 실패: ${apiResponse?.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // 네트워크 오류 메시지 표시
                        Toast.makeText(this@LoginActivity, "네트워크 오류!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    // 오류 처리
                    Toast.makeText(this@LoginActivity, "오류 발생: " + t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
