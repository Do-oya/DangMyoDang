package com.example.dangmyodang

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import com.example.dangmyodang.abandom.Abandom
import com.example.dangmyodang.abandom.RetrofitObject_abandom
import com.example.dangmyodang.shelter.RetrofitObject_shelter
import com.example.dangmyodang.shelter.Shelter
import com.example.dangmyodang.sido.RetrofitObject
import com.example.dangmyodang.sido.Sido
import com.example.dangmyodang.sigungu.RetrofitObject_sigungu
import com.example.dangmyodang.sigungu.Sigungu
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class spinnerActivity : BaseActivity(TransitionMode.HORIZON) {

    private lateinit var spinner: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var spinner3: Spinner
    private var findDate = SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.getDefault()).format(Date(System.currentTimeMillis()))
    private lateinit var listView: ListView
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)
        spinner = findViewById(R.id.spinner)
        spinner2 = findViewById(R.id.spinner2)
        spinner3 = findViewById(R.id.spinner3)
        listView = findViewById(R.id.listView)
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

        getAdaption()

    }


    private fun getAdaption() {
        Log.d("print findDate", findDate)
        // 첫 번째 스피너 어댑터 및 스피너 초기화
        val adapter1 = ArrayAdapter<String>(
            this@spinnerActivity,
            android.R.layout.simple_spinner_dropdown_item,
            mutableListOf()
        )
        val spinner1 = findViewById<Spinner>(R.id.spinner)
        spinner1.adapter = adapter1

        // 두 번째 스피너 어댑터 및 스피너 초기화
        val adapter2 = ArrayAdapter<String>(
            this@spinnerActivity,
            android.R.layout.simple_spinner_dropdown_item,
            mutableListOf()
        )
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        spinner2.adapter = adapter2

        // 세 번째 스피너 어댑터 및 스피너 초기화
        val adapter3 = ArrayAdapter<String>(
            this@spinnerActivity,
            android.R.layout.simple_spinner_dropdown_item,
            mutableListOf()
        )
        val spinner3 = findViewById<Spinner>(R.id.spinner3)
        spinner3.adapter = adapter3

        // 첫 번째 API 호출
        RetrofitObject.getApiService().getInfo("1", "20", "json").enqueue(object : Callback<Sido> {
            override fun onResponse(call: Call<Sido>, response: Response<Sido>) {
                if (response.isSuccessful) {
                    // 첫 번째 API 응답을 받았을 때
                    val sidoResponse = response.body()

                    // 스피너1 데이터 생성
                    val spinnerDataList =
                        sidoResponse?.response?.body?.items?.item?.map { it.orgdownNm }
                    if (spinnerDataList != null) {
                        setSpinnerData(spinner, spinnerDataList)

                        // 스피너1 선택 리스너
                        spinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    // 선택된 아이템에 대한 처리
                                    var selectedOrgdownNm =
                                        parent?.getItemAtPosition(position).toString()
                                    val selectedSidoItem =
                                        sidoResponse?.response?.body?.items?.item?.find { it.orgdownNm == selectedOrgdownNm }
                                    var orgCd = selectedSidoItem?.orgCd.toString()

                                    // 두 번째 API 호출 (Sigungu)
                                    RetrofitObject_sigungu.getApiService().getInfo(orgCd, "json")
                                        .enqueue(object : Callback<Sigungu> {
                                            override fun onResponse(
                                                call: Call<Sigungu>,
                                                response: Response<Sigungu>
                                            ) {
                                                if (response.isSuccessful) {
                                                    // 두 번째 API 응답을 받았을 때
                                                    val sigunguResponse = response.body()
                                                    val uprCd = orgCd

                                                    // 스피너2 데이터 생성
                                                    val spinner2DataList =
                                                        sigunguResponse?.response?.body?.items?.item?.map { it.orgdownNm }
                                                    if (spinner2DataList != null) {
                                                        setSpinnerData(spinner2, spinner2DataList)

                                                        // 스피너2 선택 리스너
                                                        spinner2.onItemSelectedListener =
                                                            object :
                                                                AdapterView.OnItemSelectedListener {
                                                                override fun onItemSelected(
                                                                    parent: AdapterView<*>?,
                                                                    view: View?,
                                                                    position: Int,
                                                                    id: Long
                                                                ) {
                                                                    // 선택된 아이템에 대한 처리

                                                                    var selectedOrgdownNm =
                                                                        parent?.getItemAtPosition(
                                                                            position
                                                                        ).toString()
                                                                    val selectedSigunguItem =
                                                                        sigunguResponse?.response?.body?.items?.item?.find { it.orgdownNm == selectedOrgdownNm }
                                                                    val orgCd_1 =
                                                                        selectedSigunguItem?.orgCd.toString()

                                                                    // 세 번째 API 호출 (Shelter)
                                                                    RetrofitObject_shelter.getApiService_shelter()
                                                                        .getInfo(
                                                                            uprCd,
                                                                            orgCd_1,
                                                                            "json"
                                                                        )
                                                                        .enqueue(object :
                                                                            Callback<Shelter> {
                                                                            override fun onResponse(
                                                                                call: Call<Shelter>,
                                                                                response: Response<Shelter>
                                                                            ) {
                                                                                if (response.isSuccessful) {
                                                                                    // 세 번째 API 응답을 받았을 때
                                                                                    val shelterResponse =
                                                                                        response.body()

                                                                                    // 스피너3 데이터 생성
                                                                                    val spinner3DataList =
                                                                                        shelterResponse?.response?.body?.items?.item?.map { it.careNm }
                                                                                    if (spinner3DataList != null) {
                                                                                        setSpinnerData(
                                                                                            spinner3,
                                                                                            spinner3DataList
                                                                                        )

                                                                                        // 스피너3 선택 리스너
                                                                                        spinner3.onItemSelectedListener =
                                                                                            object :
                                                                                                AdapterView.OnItemSelectedListener {
                                                                                                override fun onItemSelected(
                                                                                                    parent: AdapterView<*>?,
                                                                                                    view: View?,
                                                                                                    position: Int,
                                                                                                    id: Long
                                                                                                ) {
                                                                                                    // 선택된 아이템에 대한 처리
                                                                                                    val selectedItem3 =
                                                                                                        parent?.getItemAtPosition(
                                                                                                            position
                                                                                                        )
                                                                                                            .toString()
                                                                                                    var upkind =
                                                                                                        "dog"
                                                                                                    if (upkind == "dog") {
                                                                                                        upkind =
                                                                                                            "417000"
                                                                                                    } else if (upkind == "cat") {
                                                                                                        upkind =
                                                                                                            "422400"
                                                                                                    } else if (upkind == "exception") {
                                                                                                        upkind =
                                                                                                            "429900"
                                                                                                    }
                                                                                                    // 네 번째 API 호출 (Abandom)
                                                                                                    RetrofitObject_abandom.getApiService_abandom()
                                                                                                        .getInfo(
                                                                                                            "20231001",
                                                                                                            "20231101",
                                                                                                            upkind,
                                                                                                            uprCd,
                                                                                                            orgCd_1,
                                                                                                            "Y",
                                                                                                            "1",
                                                                                                            "10",
                                                                                                            "json"
                                                                                                        )
                                                                                                        .enqueue(
                                                                                                            object :
                                                                                                                Callback<Abandom> {
                                                                                                                override fun onResponse(
                                                                                                                    call: Call<Abandom>,
                                                                                                                    response: Response<Abandom>
                                                                                                                ) {
                                                                                                                    if (response.isSuccessful) {
                                                                                                                        // 네 번째 API 응답을 받았을 때
                                                                                                                        val abandomResponse =
                                                                                                                            response.body()

                                                                                                                        // 여기서 네 번째 API 응답을 처리
                                                                                                                        if(abandomResponse == null){
                                                                                                                            Toast.makeText(
                                                                                                                                applicationContext,
                                                                                                                                "body가 비어있습니다.",
                                                                                                                                Toast.LENGTH_SHORT
                                                                                                                            )
                                                                                                                        }
                                                                                                                        else{
                                                                                                                            setAbandomData(
                                                                                                                                abandomResponse
                                                                                                                            )
                                                                                                                        }



                                                                                                                        Toast.makeText(
                                                                                                                            applicationContext,
                                                                                                                            "success",
                                                                                                                            Toast.LENGTH_SHORT
                                                                                                                        )
                                                                                                                            .show()
                                                                                                                    }
                                                                                                                }

                                                                                                                override fun onFailure(
                                                                                                                    call: Call<Abandom>,
                                                                                                                    t: Throwable
                                                                                                                ) {
                                                                                                                    // 네 번째 API 호출 실패 처리
                                                                                                                    showToast(
                                                                                                                        "Failed to get Abandom data"
                                                                                                                    )
                                                                                                                }
                                                                                                            })
                                                                                                }

                                                                                                private fun setAbandomData(abandomResponse: Abandom?) {
                                                                                                    // 데이터가 없거나 비어있을 경우 처리
                                                                                                    val combinedDataList = mutableListOf<com.example.dangmyodang.AdaptionList>()
                                                                                                    if (abandomResponse == null || abandomResponse?.response?.body?.items?.item.isNullOrEmpty()) {
                                                                                                        showToast("No data available.")
                                                                                                        return
                                                                                                    }else{
                                                                                                        // 네 번째 API 응답을 처리하여 데이터 리스트 생성
                                                                                                        val processedDataList = abandomResponse.response.body.items.item.map {
                                                                                                            com.example.dangmyodang.AdaptionList(it.happenPlace ?: "", it.age ?: "",it.careAddr , it.specialMark, it.weight, it.kindCd, it.popfile ?: "")
                                                                                                        }
                                                                                                        combinedDataList.addAll(processedDataList)
                                                                                                        val adapter = MyAdapter(this@spinnerActivity, R.layout.item_layout, combinedDataList)
                                                                                                        listView.adapter = adapter

                                                                                                    }

                                                                                                }



                                                                                                override fun onNothingSelected(
                                                                                                    parent: AdapterView<*>?
                                                                                                ) {
                                                                                                    // 아무것도 선택되지 않았을 때의 처리
                                                                                                }
                                                                                            }
                                                                                    }
                                                                                } else {
                                                                                    // 세 번째 API 호출은 성공했지만 응답이 실패한 경우 처리
                                                                                    showToast("Failed to get Shelter data")
                                                                                }
                                                                            }

                                                                            override fun onFailure(
                                                                                call: Call<Shelter>,
                                                                                t: Throwable
                                                                            ) {
                                                                                // 세 번째 API 호출 실패 처리
                                                                                showToast("Failed to get Shelter data")
                                                                            }
                                                                        })
                                                                }

                                                                override fun onNothingSelected(
                                                                    parent: AdapterView<*>?
                                                                ) {
                                                                    // 아무것도 선택되지 않았을 때의 처리
                                                                }
                                                            }
                                                    }
                                                } else {
                                                    // 두 번째 API 호출은 성공했지만 응답이 실패한 경우 처리
                                                    showToast("Failed to get Sigungu data")
                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<Sigungu>,
                                                t: Throwable
                                            ) {
                                                // 두 번째 API 호출 실패 처리
                                                showToast("Failed to get Sigungu data")
                                            }
                                        })
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // 아무것도 선택되지 않았을 때의 처리
                                }
                            }
                    }
                } else {
                    // API 호출은 성공했지만 응답이 실패한 경우 처리
                    showToast("Failed to get Sido data")
                }
            }

            override fun onFailure(call: Call<Sido>, t: Throwable) {
                // API 호출 실패 처리
                showToast("Failed to get Sido data")
            }
        })
    }

    private fun setSpinnerData(spinner: Spinner, dataList: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dataList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}


