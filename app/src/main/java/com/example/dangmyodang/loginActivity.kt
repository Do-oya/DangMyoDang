package com.example.dangmyodang

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

data class LoginData(
    val username: String,
    val password: String
)

class LoginActivity : BaseActivity(TransitionMode.HORIZON) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val join = findViewById<Button>(R.id.join)
        join.setOnClickListener { // 회원가입
            val intent = Intent(this, joinActivity::class.java)
            startActivity(intent)
        }

        val usernameEditText = findViewById<EditText>(R.id.id)
        val passwordEditText = findViewById<EditText>(R.id.pw)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-18-217-86-118.us-east-2.compute.amazonaws.com:3306/") // 실제 서버 URL로 변경
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
                            // 로그인 성공 시 다음 화면으로 이동
                            val userName = "댕묘댕"
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("userName", userName)  // 여기서 userName은 사용자 이름 변수
                            startActivity(intent)
                            fun someFunction() {
                                finish()

                                // 이 화면은, 오른쪽에서 왼쪽으로 슬라이딩 하면서 사라집니다.
                                overridePendingTransition(R.anim.none, R.anim.horizon_exit)
                            }
                        } else {
                            // 로그인 실패 메시지 표시
                            Toast.makeText(this@LoginActivity, "로그인 실패: 입력된 정보를 확인하세요", Toast.LENGTH_SHORT).show()
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

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}
