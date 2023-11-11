package com.example.dangmyodang

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView

class dog_bingActivity : BaseActivity(TransitionMode.HORIZON) {
    private lateinit var vvvvvvv: VideoView
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_bing)

        vvvvvvv = findViewById(R.id.vvvvvvv)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)

        val videoUri = Uri.parse("android.resource://$packageName/" + R.raw.dog_bing)
        vvvvvvv.setMediaController(MediaController(this))
        vvvvvvv.setVideoURI(videoUri)

        playButton.setOnClickListener {
            if (!vvvvvvv.isPlaying) {
                vvvvvvv.start()
            }
        }

        pauseButton.setOnClickListener {
            if (vvvvvvv.isPlaying) {
                vvvvvvv.pause()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (vvvvvvv.isPlaying) vvvvvvv.pause()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}

