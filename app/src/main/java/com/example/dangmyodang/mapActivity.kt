package com.example.dangmyodang

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class mapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    // Retrofit 서비스 인터페이스
    interface NaverLocationService {
        @GET("v1/search/local.json")
        fun searchLocation(
            @Query("query") query: String,  // 검색어
            @Query("display") display: Int,  // 검색 결과 출력 건수
            @Query("start") start: Int,      // 검색 시작 위치
            @Query("sort") sort: String      // 정렬 기준
        ): Call<LocationResponse>
    }

    // 모델 클래스
    data class LocationResponse(
        val items: List<LocationItem>
    )

    data class LocationItem(
        val title: String,
        val address: String,
        val mapx: Double,
        val mapy: Double
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
        mapFragment.getMapAsync(this)

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)

        // 검색 버튼 클릭 이벤트 처리
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                searchLocation(query)
            }
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        // 위치 추적 모드 설정
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        // 가상 위치로 시작 위치 설정
        setVirtualLocation()
    }
    private fun setVirtualLocation() {
        // 가상 위치의 좌표를 설정
        val virtualLatitude = 37.8094214  // 가상 위도
        val virtualLongitude = 127.070506  // 가상 경도

        // 가상 위치를 LatLng 객체로 생성
        val virtualLocation = LatLng(virtualLatitude, virtualLongitude)

        // 마커 추가
        val marker = Marker()
        marker.position = virtualLocation
        marker.icon = OverlayImage.fromResource(R.drawable.icon) // 마커 아이콘 설정
        marker.width = resources.getDimensionPixelSize(R.dimen.icon_width)
        marker.height = resources.getDimensionPixelSize(R.dimen.icon_height)
        marker.map = naverMap

        // 지도를 가상 위치로 이동
        val cameraUpdate = CameraUpdate.scrollTo(virtualLocation)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun searchLocation(query: String) {
        // Retrofit을 초기화
        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create()) // 문자열 응답 처리를 위해 추가
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("X-Naver-Client-Id", "cwEnJDcKhtmJOTi0FnGM")
                    .addHeader("X-Naver-Client-Secret", "e5hG08vB46")
                    .build()
                chain.proceed(newRequest)
            }.build())
            .build()

        val naverLocationService = retrofit.create(NaverLocationService::class.java)

        // Retrofit을 사용한 Location Search API 요청
        naverLocationService.searchLocation(query, 10, 1, "random").enqueue(object :
            Callback<LocationResponse> {
            override fun onResponse(call: Call<LocationResponse>, response: Response<LocationResponse>) {
                if (response.isSuccessful) {
                    val items = response.body()?.items

                    if (items != null && items.isNotEmpty()) {
                        val firstItem = items[0]  // 첫 번째 검색 결과 사용
                        val targetLocation = LatLng(firstItem.mapy, firstItem.mapx)

                        // 좌표를 사용하여 지도 이동
                        moveToLocation(targetLocation)
                    } else {
                        // 검색 결과가 없는 경우 처리
                        println("No search results.")
                    }
                } else {
                    // 서버로부터 응답을 받지 못한 경우 처리
                    println("Response unsuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                // 실패 시 처리
                println("Search failed: ${t.message}")
            }
        })
    }

    private fun moveToLocation(targetLocation: LatLng) {
        // 마커 추가
        val marker = Marker()
        marker.position = targetLocation
        marker.icon = OverlayImage.fromResource(R.drawable.icon)
        marker.width = resources.getDimensionPixelSize(R.dimen.icon_width)
        marker.height = resources.getDimensionPixelSize(R.dimen.icon_height)
        marker.map = naverMap

        // 지도를 해당 위치로 이동
        val cameraUpdate = CameraUpdate.scrollTo(targetLocation)
        naverMap.moveCamera(cameraUpdate)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.vertical_exit)
        }
    }
}