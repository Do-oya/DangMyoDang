package com.example.dangmyodang

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

class aActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnimalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aactivity)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AnimalAdapter()
        recyclerView.adapter = adapter

        val apiKey = "0uipEIPTH1g8CSOsCtOGEECkI9nFgfijlyKXlf8k1UdScSNV30rnzbGG6HXFA9lkcFGTcM4jNAAzm8Kb0X%2F2Qg%3D%3D"
        val retrofit = Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/1543061/") // API의 엔드포인트로 변경
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val animalApi = retrofit.create(AnimalApi::class.java)

        // Retrofit 헤더에 API 키 추가
        val call = animalApi.getAnimalData(apiKey)
        call.enqueue(object : Callback<AnimalData> {
            override fun onResponse(call: Call<AnimalData>, response: Response<AnimalData>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    // 데이터 사용
                    data?.let {
                        adapter.setData(it.row)
                        adapter.notifyDataSetChanged()
                        Log.d("Response", "Data loaded successfully: ${data.list_total_count} items")
                    }
                } else {
                    // 오류 처리
                    Log.e("Response", "Response not successful: ${response.code()}")
                    // API 응답이 실패한 경우 처리
                }
            }

            override fun onFailure(call: Call<AnimalData>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("Error", "Network error: ${t.message}")
                // 네트워크 연결 오류 처리
            }
        })
    }
}

interface AnimalApi {
    @GET("abandonmentPublicSrvc") // 실제 API 엔드포인트로 변경
    fun getAnimalData(@Header("Authorization") apiKey: String): Call<AnimalData>
}

data class AnimalData(
    val list_total_count: Int,
    val RESULT: Result,
    val row: List<Row>
)

data class Result(
    val CODE: String,
    val MESSAGE: String
)

data class Row(
    val ANIMAL_NO: Int,
    val PHOTO_KND: String,
    val PHOTO_NO: Int,
    val PHOTO_URL: String
)

class AnimalAdapter : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {
    private val animalList = mutableListOf<Row>()

    fun setData(newData: List<Row>) {
        animalList.clear()
        animalList.addAll(newData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animalList[position]
        holder.bind(animal)
    }

    override fun getItemCount(): Int {
        return animalList.size
    }

    inner class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(animal: Row) {
            val photoImageView = itemView.findViewById<ImageView>(R.id.photoImageView)
            val photoKndTextView = itemView.findViewById<TextView>(R.id.photoKndTextView)

            // 데이터를 View에 설정
            photoKndTextView.text = animal.PHOTO_KND

            // Glide를 사용하여 이미지 로딩
            Glide.with(itemView)
                .load(animal.PHOTO_URL) // 이미지 URL
                .into(photoImageView)
        }
    }
}
