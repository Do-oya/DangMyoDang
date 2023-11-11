package com.example.dangmyodang

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView

class dog_handActivity : BaseActivity(TransitionMode.HORIZON) {
    private lateinit var vvvvv: VideoView
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_hand)

        vvvvv = findViewById(R.id.vvvvv)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)

        val videoUri = Uri.parse("android.resource://$packageName/" + R.raw.dog_hand)
        vvvvv.setMediaController(MediaController(this))
        vvvvv.setVideoURI(videoUri)

        playButton.setOnClickListener {
            if (!vvvvv.isPlaying) {
                vvvvv.start()
            }
        }

        pauseButton.setOnClickListener {
            if (vvvvv.isPlaying) {
                vvvvv.pause()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (vvvvv.isPlaying) vvvvv.pause()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}

