package com.example.dangmyodang

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson

class MapActivity : BaseActivity(TransitionMode.HORIZON), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var googleMap: GoogleMap
    private lateinit var medicalButton: Button
    private lateinit var playgroundButton: Button
    private lateinit var equipmentButton: Button
    private lateinit var animalInfo: AnimalInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        medicalButton = findViewById(R.id.button)
        playgroundButton = findViewById(R.id.button2)
        equipmentButton = findViewById(R.id.button3)

        medicalButton.setOnClickListener { showFacilitiesOfType("medical") }
        playgroundButton.setOnClickListener { showFacilitiesOfType("playground") }
        equipmentButton.setOnClickListener { showFacilitiesOfType("equipment") }

        // 동물 시설들의 정보를 읽어옴
        try {
            val jsonString = this@MapActivity.assets.open("dongbanall.json").bufferedReader().readText()
            animalInfo = Gson().fromJson(jsonString, AnimalInfo::class.java)
        } catch (e: Exception) {
            Log.e("MapActivity", "Error loading JSON file: ${e.message}")
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val myLocation = floatArrayOf(37.5652561900958f, 126.97715862212084f)
        val allowedRadius = 3000f // 원하는 반경 (미터)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(myLocation[0].toDouble(), myLocation[1].toDouble()), 15F))

        googleMap.setOnMarkerClickListener(this)

        // 초기에는 모든 시설을 표시
        showFacilitiesOfType("all")
    }

    private fun showFacilitiesOfType(type: String) {
        // 해당 버튼에 맞는 시설만 마커로 표시
        googleMap.clear() // 이전에 추가된 마커들을 지우고 새로운 마커를 추가합니다.

        val myLocation = floatArrayOf(37.5652561900958f, 126.97715862212084f)
        val allowedRadius = 3000f // 원하는 반경 (미터)

        // 동물 시설들의 위치를 지도에 표시
        animalInfo.animal.forEach {
            val animalLocation = LatLng(it.latitude, it.longitude)
            val results = FloatArray(1)

            Location.distanceBetween(
                myLocation[0].toDouble(),
                myLocation[1].toDouble(),
                it.latitude,
                it.longitude,
                results
            )

            val distance = results[0]

            if (distance <= allowedRadius && (type == "all" || isMatchingType(type, it.facilityType))) {
                // type이 "all"이거나, type에 맞는 경우에만 마커 추가
                addMarker(it, animalLocation)
            }
        }
    }

    private fun isMatchingType(type: String, facilityType: String): Boolean {
        return when (type) {
            "medical" -> facilityType == "동물약국" || facilityType == "동물병원" || facilityType == "일반동물병원"
            "playground" -> facilityType == "애견카페" || facilityType == "애견 동반 펜션"
            "equipment" -> facilityType == "강아지용품" || facilityType == "반려동물용품"
            else -> false
        }
    }

    private fun addMarker(animal: AnimalInfo.Animal, location: LatLng) {
        val markerOptions = MarkerOptions()
            .position(location)
            .title(animal.name)

        if (animal.facilityType == "동물약국") {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        }
        if (animal.facilityType == "일반동물병원") {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        }
        if (animal.facilityType == "동물병원") {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        }

        if (animal.facilityType == "애견카페") {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        }
        if (animal.facilityType == "애견 동반 펜션") {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        }

        if (animal.facilityType == "강아지용품") {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        }
        if (animal.facilityType == "반려동물용품") {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        }

        val marker = googleMap.addMarker(markerOptions)
        marker?.tag = animal // 마커에 정보를 태그로 저장
    }

    override fun onMarkerClick(marker: com.google.android.gms.maps.model.Marker): Boolean {
        // 마커를 클릭했을 때 실행되는 로직
        val animalInfo = marker.tag as? AnimalInfo.Animal

        if (animalInfo != null) {
            // 마커 클릭 시에 상세 정보를 보여주는 다이얼로그
            showAnimalInfoDialog(animalInfo)
        } else {
            Log.e("MapActivity", "Error retrieving animal information from marker tag.")
        }

        return true
    }

    private fun showAnimalInfoDialog(animalInfo: AnimalInfo.Animal) {
        // AlertDialog를 사용하여 상세 정보를 표시
        AlertDialog.Builder(this)
            .setTitle(animalInfo.name)
            .setMessage(
                "시설 유형: ${animalInfo.facilityType}\n" +
                        "전화번호: ${animalInfo.tell}\n" +
                        "영업 시간: ${animalInfo.time}\n" +
                        "휴무일: ${animalInfo.holly}\n" +
                        "주소: ${animalInfo.address}"
            )
            .setPositiveButton("확인", null)
            .show()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}
