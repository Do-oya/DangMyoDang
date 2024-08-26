package com.example.dangmyodang

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface PostApiService {
    @GET("getPosts")
    fun getPosts(): Call<List<Post>>
}

data class Post(val title: String, val content: String)

class communityActivity : BaseActivity(TransitionMode.HORIZON) {

    private val postList = mutableListOf<String>()
    private lateinit var posts: List<Post>
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

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

        val postListView: ListView = findViewById(R.id.postListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, postList)
        postListView.adapter = adapter

        postListView.setOnItemClickListener { _, _, position, _ ->
            // 여기에 게시글 상세 정보로 이동하는 코드 추가
            val intent = Intent(this, PostDetailActivity::class.java)
            // 선택된 게시글의 제목과 내용을 전달
            val selectedPost = posts.getOrNull(position)
            selectedPost?.let {
                intent.putExtra("title", it.title)
                intent.putExtra("content", it.content)
                startActivity(intent)
            }
        }

        val btnCreatePost = findViewById<ImageButton>(R.id.btnCreatePost)
        btnCreatePost.setOnClickListener {
            startActivity(Intent(this, community_page_Activity::class.java))
        }

        // 서버에서 게시글 목록을 가져와 어댑터를 업데이트하는 함수 호출
        fetchPostsFromServer(adapter)
    }

    private fun fetchPostsFromServer(adapter: ArrayAdapter<String>) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-18-224-33-245.us-east-2.compute.amazonaws.com:3306/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val postApiService = retrofit.create(PostApiService::class.java)

        postApiService.getPosts().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    // 서버에서 가져온 게시글 목록을 어댑터에 추가하고 알림
                    posts = response.body() ?: emptyList() // posts 변수에 응답을 저장
                    postList.addAll(posts.map { it.title })
                    adapter.notifyDataSetChanged()
                } else {
                    Log.e("CommunityActivity", "Failed to fetch posts: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.e("CommunityActivity", "Error fetching posts", t)
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}
