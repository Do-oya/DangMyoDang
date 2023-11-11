package com.example.dangmyodang

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView

class dog_comoneActivity : BaseActivity(TransitionMode.HORIZON) {
    private lateinit var vvvv: VideoView
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_comone)

        vvvv = findViewById(R.id.vvvv)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)

        val videoUri = Uri.parse("android.resource://$packageName/" + R.raw.dog_comone)
        vvvv.setMediaController(MediaController(this))
        vvvv.setVideoURI(videoUri)

        playButton.setOnClickListener {
            if (!vvvv.isPlaying) {
                vvvv.start()
            }
        }

        pauseButton.setOnClickListener {
            if (vvvv.isPlaying) {
                vvvv.pause()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (vvvv.isPlaying) vvvv.pause()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}

