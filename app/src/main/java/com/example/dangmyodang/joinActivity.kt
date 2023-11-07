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
import retrofit2.http.Body
import retrofit2.http.POST

data class UserData(
    val username: String,
    val email: String,
    val password: String
)

data class ApiResponse(
    val success: Boolean,
    val message: String
)
data class UsernameData(
    val username: String
)

interface MyApi {
    @POST("register")
    fun registerUser(@Body userData: UserData): Call<ApiResponse>
    @POST("login")
    fun loginUser(@Body loginData: LoginData): Call<ApiResponse>
    @POST("checkUsername") // 아이디 중복 체크를 위한 엔드포인트 추가
    fun checkUsername(@Body usernameData: UsernameData): Call<ApiResponse>
}

class joinActivity : BaseActivity(TransitionMode.VERTICAL) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val userIDEditText = findViewById<EditText>(R.id.userID)
        val userNameEditText = findViewById<EditText>(R.id.userName)
        val passwordEditText = findViewById<EditText>(R.id.pw1)
//        val checkButton = findViewById<Button>(R.id.check)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-18-226-34-113.us-east-2.compute.amazonaws.com:3306/") // 실제 서버 URL로 변경
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val api = retrofit.create(MyApi::class.java)
//        checkButton.setOnClickListener {
//            val username = userIDEditText.text.toString()
//
//            if (username.isEmpty()) {
//                // 아이디를 입력하지 않았을 때 메시지 표시
//                Toast.makeText(this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

            // 서버에 아이디 중복 체크 요청을 보내고 응답을 처리
//            val api = retrofit.create(MyApi::class.java)
//            val usernameData = UsernameData(username)
//            val call = api.checkUsername(usernameData)
//
//            call.enqueue(object : Callback<ApiResponse> {
//                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
//                    if (response.isSuccessful) {
//                        val apiResponse = response.body()
//                        if (apiResponse?.success == true) {
//                            // 아이디가 사용 가능한 경우
//                            Toast.makeText(this@joinActivity, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
//                        } else {
//                            // 아이디가 이미 사용 중인 경우
//                            Toast.makeText(this@joinActivity, "이미 사용 중인 아이디입니다.", Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
//                        // 네트워크 오류 메시지 표시
//                        println("네트워크 오류!")
//                    }
//                }
//
//                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//                    // 오류 처리
//                    Toast.makeText(this@joinActivity, "오류 발생: " + t.message, Toast.LENGTH_SHORT).show()
//                    println("오류 발생: " + t.message)
//                }
//            })
//        }

        val join1 = findViewById<Button>(R.id.join1)
        join1.setOnClickListener {
            val username = userIDEditText.text.toString()
            val email = userNameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || email.isEmpty() || passwordEditText.text.toString().isEmpty()) {
                // 필수 필드를 모두 입력하도록 메시지를 표시
                Toast.makeText(this@joinActivity, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 필수 필드가 입력되었을 때만 회원가입 요청을 보냄
                val userData = UserData(username, email, password)

                val call = api.registerUser(userData)

                call.enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            if (apiResponse?.success == true) {
                                // 회원가입 성공 메시지 표시
                                Toast.makeText(this@joinActivity, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@joinActivity, LoginActivity::class.java)
                                startActivity(intent)
                                println("회원가입 성공!")
                            } else {
                                // 회원가입 실패 메시지 표시
                                Toast.makeText(this@joinActivity, "회원가입 실패!", Toast.LENGTH_SHORT).show()
                                println("회원가입 실패: ${apiResponse?.message}")
                            }
                        } else {
                            // 네트워크 오류 메시지 표시
                            println("네트워크 오류!")
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        // 오류 처리
                        Toast.makeText(this@joinActivity, "오류 발생: " + t.message, Toast.LENGTH_SHORT).show()
                        println("오류 발생: " + t.message)
                    }
                })
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.vertical_exit)
        }
    }
}
