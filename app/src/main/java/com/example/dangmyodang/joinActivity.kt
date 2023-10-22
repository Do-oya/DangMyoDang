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
import retrofit2.http.Body
import retrofit2.http.POST

data class UserData(
    val username: String,
    val email: String,
    val password: Int
)

data class ApiResponse(
    val success: Boolean,
    val message: String
)

interface MyApi {
    @POST("register")
    fun registerUser(@Body userData: UserData): Call<ApiResponse>
    @POST("login")
    fun loginUser(@Body loginData: LoginData): Call<ApiResponse>
}

class joinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val usernameEditText = findViewById<EditText>(R.id.name)
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.pw1)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000") // 실제 서버 URL로 변경
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val api = retrofit.create(MyApi::class.java)

        val join1 = findViewById<Button>(R.id.join1)
        join1.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString().toInt()

            val userData = UserData(username, email, password)

            val call = api.registerUser(userData)

            call.enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse?.success == true) {
                            // 회원가입 성공 메시지 표시
                            Toast.makeText(this@joinActivity, "회원가입 성공!", Toast.LENGTH_SHORT).show()
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

        val cancel = findViewById<Button>(R.id.cancel)
        cancel.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
