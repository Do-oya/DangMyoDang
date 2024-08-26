package com.example.dangmyodang

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

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

        // FusedLocationProviderClient 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        if (checkLocationPermission()) {
            googleMap.isMyLocationEnabled = true // 현재 위치 활성화

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLocation = LatLng(it.latitude, it.longitude)

                    // 내 위치를 기본 파란색 동그라미로 표시
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
                }
            }

            googleMap.setOnMarkerClickListener(this)

            // 초기에는 모든 시설을 표시
            showFacilitiesOfType("all")
        }
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 없으면 권한 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return false
        }
        return true
    }

    private fun showFacilitiesOfType(type: String) {
        googleMap.clear()

        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val myLocation = LatLng(it.latitude, it.longitude)
                    val allowedRadius = 3000f // 원하는 반경 (미터)

                    // 동물 시설들의 위치를 지도에 표시
                    animalInfo.animal.forEach {
                        val animalLocation = LatLng(it.latitude, it.longitude)
                        val results = FloatArray(1)

                        Location.distanceBetween(
                            myLocation.latitude,
                            myLocation.longitude,
                            it.latitude,
                            it.longitude,
                            results
                        )

                        val distance = results[0]

                        if (distance <= allowedRadius && (type == "all" || isMatchingType(type, it.facilityType))) {
                            addMarker(it, animalLocation)
                        }
                    }
                }
            }
        }
    }

    private fun isMatchingType(type: String, facilityType: String): Boolean {
        val types = facilityType.split(",").map { it.trim() }

        return when (type) {
            "medical" -> types.any {
                it == "동물약국" || it == "동물병원" || it == "일반동물병원" ||
                        it == "강아지미용" || it == "축산동물병원" || it == "강아지미용,예약제" || it == "예약제"
            }
            "playground" -> types.any {
                it == "애견카페" || it == "애견 동반 펜션" || it == "공원" ||
                        it == "미술관" || it == "박물관" || it == "애견카페,용품" || it == "문예회관"
            }
            "equipment" -> types.any {
                it == "강아지용품" || it == "반려동물용품" || it == "애견카페,용품" || it == "용품"
            }
            else -> false
        }
    }


    private fun addMarker(animal: AnimalInfo.Animal, location: LatLng) {
        val markerOptions = MarkerOptions()
            .position(location)
            .title(animal.name)

        // 초록색 마커 - 의료시설
        if (isMedicalFacility(animal.facilityType)) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        }
        // 노랑색 마커 - 놀이시설
        else if (isPlayground(animal.facilityType)) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        }
        // 파란색 마커 - 용품시설
        else if (isEquipmentFacility(animal.facilityType)) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        }

        val marker = googleMap.addMarker(markerOptions)
        marker?.tag = animal // 마커에 정보를 태그로 저장
    }

    private fun isMedicalFacility(facilityType: String): Boolean {
        val types = listOf("동물약국", "일반동물병원", "동물병원", "강아지미용", "축산동물병원", "강아지미용,예약제", "예약제")
        return checkFacilityType(facilityType, types)
    }

    private fun isPlayground(facilityType: String): Boolean {
        val types = listOf("애견카페", "애견 동반 펜션", "공원", "박물관", "미술관", "애견카페,용품", "문예회관")
        return checkFacilityType(facilityType, types)
    }

    private fun isEquipmentFacility(facilityType: String): Boolean {
        val types = listOf("강아지용품", "반려동물용품", "용품")
        return checkFacilityType(facilityType, types)
    }

    private fun checkFacilityType(facilityType: String, validTypes: List<String>): Boolean {
        return validTypes.any { it in facilityType.split(",") }
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