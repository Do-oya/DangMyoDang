package com.example.dangmyodang

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ExerciseResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("exerciseRecords") val exerciseRecords: List<ExerciseRecord>?
)

class running_hs_Activity : BaseActivity(TransitionMode.HORIZON) {

    private lateinit var textViewRecords: TextView
    private lateinit var exerciseApiService: ExerciseApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running_hs)

        textViewRecords = findViewById(R.id.textViewRecords)

        // Retrofit 설정
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-18-224-33-245.us-east-2.compute.amazonaws.com:3306/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .build()

        exerciseApiService = retrofit.create(ExerciseApiService::class.java)

        // 사용자 ID는 임의로 설정했습니다. 실제 사용자 ID로 교체해야 합니다.
        val userId = 1

        // DatePicker에서 사용자가 선택한 날짜를 받아옴
        val datePicker: DatePicker = findViewById(R.id.datePicker)
        val buttonShowRecords: Button = findViewById(R.id.buttonShowRecords)

        buttonShowRecords.setOnClickListener {
            val selectedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(
                Date(datePicker.year - 1900, datePicker.month, datePicker.dayOfMonth)
            )
            loadExerciseRecords(userId, selectedDate)
        }
    }

    private fun loadExerciseRecords(userId: Int, selectedDate: String) {
        exerciseApiService.getExerciseRecordsByDate(userId, selectedDate)
            .enqueue(object : Callback<ExerciseResponse> {
                override fun onResponse(
                    call: Call<ExerciseResponse>,
                    response: Response<ExerciseResponse>
                ) {
                    if (response.isSuccessful) {
                        val exerciseResponse = response.body()
                        if (exerciseResponse?.success == true) {
                            val exerciseRecords = exerciseResponse.exerciseRecords
                            exerciseRecords?.let {
                                displayExerciseRecords(it)
                            }
                        } else {
                            textViewRecords.text = "서버에서 데이터를 가져오는 데 실패했습니다."
                        }
                    } else {
                        textViewRecords.text = "서버에서 데이터를 가져오는 데 실패했습니다."
                    }
                }

                override fun onFailure(call: Call<ExerciseResponse>, t: Throwable) {
                    textViewRecords.text = "서버 통신에 실패했습니다. 에러: ${t.message}"
                }
            })
    }

    private fun displayExerciseRecords(exerciseRecords: List<ExerciseRecord>?) {
        if (exerciseRecords != null && exerciseRecords.isNotEmpty()) {
            // 운동 기록이 있을 경우 텍스트뷰에 표시
            val recordsText = exerciseRecords.joinToString("\n\n") {
                Log.d("ExerciseRecord", "Debug - Start Time: ${it.startTime}, End Time: ${it.endTime}")
                        "이동 거리: ${it.distance} m\n" +
                        "평균 속도: ${it.averageSpeed} m/s"
            }

            textViewRecords.text = recordsText
        } else {
            // 운동 기록이 없을 경우 메시지 표시
            textViewRecords.text = "선택한 날짜에 운동 기록이 없습니다."
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}
