package com.example.dangmyodang

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView

class dog_pushActivity : BaseActivity(TransitionMode.HORIZON) {
    private lateinit var vvvvvv: VideoView
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_push)

        vvvvvv = findViewById(R.id.vvvvvv)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)

        val videoUri = Uri.parse("android.resource://$packageName/" + R.raw.dog_push)
        vvvvvv.setMediaController(MediaController(this))
        vvvvvv.setVideoURI(videoUri)

        playButton.setOnClickListener {
            if (!vvvvvv.isPlaying) {
                vvvvvv.start()
            }
        }

        pauseButton.setOnClickListener {
            if (vvvvvv.isPlaying) {
                vvvvvv.pause()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (vvvvvv.isPlaying) vvvvvv.pause()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}

