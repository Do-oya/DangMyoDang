package com.example.dangmyodang

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.moshi.Json
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class PostRequest(
    @Json(name = "title") val title: String,
    @Json(name = "content") val content: String
)

interface ApiService {
    @POST("savePost")
    fun savePost(@Body post: PostRequest): Call<Void>
}

class community_page_Activity : BaseActivity(TransitionMode.HORIZON) {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://ec2-18-224-33-245.us-east-2.compute.amazonaws.com:3306/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
    private lateinit var bottomNavigationView: BottomNavigationView

    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_page)

        val editTextTitle = findViewById<EditText>(R.id.editTitle)
        val editTextContent = findViewById<EditText>(R.id.editContent)
        val btnCreatePost = findViewById<Button>(R.id.btnCreatePost)

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

        btnCreatePost.setOnClickListener {
            val title = editTextTitle.text.toString()
            val content = editTextContent.text.toString()

            // Retrofit을 사용하여 서버로 데이터 전송
            val postRequest = PostRequest(title, content)
            val call = apiService.savePost(postRequest)

            call.enqueue(object : retrofit2.Callback<Void> {
                override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("community_page_Activity", "Post saved successfully")
                    } else {
                        Log.e("community_page_Activity", "Failed to save post: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("community_page_Activity", "Error saving post", t)
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
