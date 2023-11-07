package com.example.dangmyodang

import android.location.Location
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapActivity : BaseActivity(TransitionMode.VERTICAL) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            val jsonString = this@MapActivity.assets.open("dongban.json").bufferedReader().readText()
            val jsonType = object : TypeToken<AnimalInfo>() {}.type
            val animalInfo = Gson().fromJson(jsonString, jsonType) as AnimalInfo

            // 가상 위치 설정 부분 주석 처리
            /*
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val mockLocationProvider = LocationManager.GPS_PROVIDER

            val location = Location(mockLocationProvider)
            location.latitude = 37.5652561900958 // 원하는 가상 위도 설정
            location.longitude = 126.97715862212084 // 원하는 가상 경도 설정

            locationManager.addTestProvider(mockLocationProvider, false, false, false, false, false, true, true, 1, 2)
            locationManager.setTestProviderEnabled(mockLocationProvider, true)
            locationManager.setTestProviderLocation(mockLocationProvider, location)

            val myLocation = Location(mockLocationProvider)
            myLocation.latitude = 37.5652561900958
            myLocation.longitude = 126.97715862212084

            val allowedRadius = 1000 // 원하는 반경 (미터)
            */

            // 가상 위치 설정 부분 주석 처리 후 코드 실행
            val myLocation = Location("myLocation")
            myLocation.latitude = 37.5652561900958
            myLocation.longitude = 126.97715862212084

            val allowedRadius = 3000 // 원하는 반경 (미터)

            animalInfo.animal.forEach {
                val animalLocation = Location("animalLocation")
                animalLocation.latitude = it.latitude
                animalLocation.longitude = it.longitude

                val distance = myLocation.distanceTo(animalLocation)

                if (distance <= allowedRadius) {
                    googleMap.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title(it.name))
                }
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.5652561900958, 126.97715862212084), 15F))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.vertical_exit)
        }
    }
}
