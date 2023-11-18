package com.example.dangmyodang

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.PolylineOverlay
import com.naver.maps.map.util.FusedLocationSource
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.text.SimpleDateFormat
import java.util.*

interface ExerciseApiService {
    @POST("/saveExerciseRecord")
    fun saveExerciseRecord(@Body record: ExerciseRecord): Call<Void>
}

data class ExerciseRecord(
    val userId: Int,
    val startTime: String,
    val endTime: String,
    val distance: Double,
    val averageSpeed: Double,
    val imagePath: String?
)

class RunningActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val routeCoordinates = mutableListOf<LatLng>()
    private var polylineOverlay: PolylineOverlay? = null

    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var goButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var distanceTextView: TextView
    private lateinit var mapFragment: MapFragment

    private var isTracking = false
    private var startTime = 0L
    private var totalDistance = 0.0

    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
                ?: MapFragment.newInstance().also {
                    supportFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
                }

        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val lastLocation = locationResult.lastLocation
                lastLocation?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    onLocationChanged(latLng)
                }
            }
        }

        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        timerTextView = findViewById(R.id.timertextview)
        distanceTextView = findViewById(R.id.distanceTextView)
        goButton = findViewById(R.id.goButton)

        goButton.setOnClickListener {
            val intent = Intent(this, running_hs_Activity::class.java)
            startActivity(intent)
        }

        startButton.setOnClickListener {
            startLocationUpdates()
            startButton.visibility = View.GONE
            stopButton.visibility = View.VISIBLE
            startTime = System.currentTimeMillis()
            isTracking = true
            updateTimer()
        }

        stopButton.setOnClickListener {
            stopLocationUpdates()
            startButton.visibility = View.VISIBLE
            stopButton.visibility = View.GONE
            isTracking = false

            // 운동 종료 시 서버에 기록 저장
            saveExerciseRecordToServer(null)
        }
    }

    private fun startLocationUpdates() {
        try {
            val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun onLocationChanged(newLatLng: LatLng) {
        Log.d("MapDebug", "Received new coordinates: $newLatLng")
        naverMap.moveCamera(CameraUpdate.scrollTo(newLatLng))
        routeCoordinates.add(newLatLng)
        drawPolyline()

        if (isTracking) {
            updateDistance()
            updateTimer()
        }
    }

    private fun drawPolyline() {
        if (routeCoordinates.size >= 2) {
            if (polylineOverlay == null) {
                polylineOverlay = PolylineOverlay().apply {
                    color = Color.BLUE
                    width = 5
                    map = naverMap
                }
            }
            polylineOverlay?.coords = routeCoordinates
        }
    }

    private fun updateDistance() {
        if (routeCoordinates.size >= 2) {
            val lastIndex = routeCoordinates.size - 1
            val previousLatLng = routeCoordinates[lastIndex - 1]
            val currentLatLng = routeCoordinates[lastIndex]
            val distance = calculateDistance(previousLatLng, currentLatLng)
            totalDistance += distance
            distanceTextView.text = String.format("거리: %.2f m", totalDistance)
        }
    }

    private fun calculateDistance(start: LatLng, end: LatLng): Double {
        val radius = 6371e3  // Earth radius in meters
        val lat1 = Math.toRadians(start.latitude)
        val lon1 = Math.toRadians(start.longitude)
        val lat2 = Math.toRadians(end.latitude)
        val lon2 = Math.toRadians(end.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return radius * c
    }

    private fun updateTimer() {
        if (isTracking) {
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - startTime
            val seconds = (elapsedTime / 1000).toInt()
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            timerTextView.text = String.format("%02d:%02d", minutes, remainingSeconds)
            timerTextView.postDelayed({ updateTimer() }, 1000)
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        this.naverMap.locationSource = locationSource

        if (checkLocationPermission()) {
            naverMap.locationTrackingMode = LocationTrackingMode.Follow

            val lastLocation = locationSource.lastLocation
            lastLocation?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                naverMap.moveCamera(CameraUpdate.scrollTo(latLng))
                Log.d("MapDebug", "Move camera called")
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        val fineLocationPermissionState = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermissionState = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        return (fineLocationPermissionState == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermissionState == PackageManager.PERMISSION_GRANTED)
    }

    private fun saveExerciseRecordToServer(imagePath: String?) {
        // 이미지 경로를 이용하여 ExerciseRecord 객체 생성
        val userId = 1 // 실제 사용자 ID로 교체
        val endTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(System.currentTimeMillis()))
        val startTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(startTime))
        val distance = totalDistance
        val averageSpeed = calculateAverageSpeed()

        val exerciseRecord = ExerciseRecord(userId, startTime, endTime, distance, averageSpeed, imagePath)

        // 서버로 ExerciseRecord 객체 전송
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-15-195-215.us-east-2.compute.amazonaws.com:3306/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val exerciseApiService = retrofit.create(ExerciseApiService::class.java)
        val call = exerciseApiService.saveExerciseRecord(exerciseRecord)

        call.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    // 성공적으로 전송됨
                    Log.d("ExerciseRecord", "Exercise record saved successfully!")
                } else {
                    // 서버로의 전송 실패
                    Log.e("ExerciseRecord", "Failed to save exercise record. Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // 통신 실패
                Log.e("ExerciseRecord", "Failed to communicate with the server. Error: ${t.message}")
            }
        })
    }

    private fun calculateAverageSpeed(): Double {
        // TODO: 평균 속도 계산 로직 구현
        return 0.0
    }
}
