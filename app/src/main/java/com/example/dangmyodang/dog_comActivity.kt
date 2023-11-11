package com.example.dangmyodang

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView

class dog_comActivity : BaseActivity(TransitionMode.HORIZON) {
    private lateinit var vvv: VideoView
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_com)

        vvv = findViewById(R.id.vvv)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)

        val videoUri = Uri.parse("android.resource://$packageName/" + R.raw.dog_com)
        vvv.setMediaController(MediaController(this))
        vvv.setVideoURI(videoUri)

        playButton.setOnClickListener {
            if (!vvv.isPlaying) {
                vvv.start()
            }
        }

        pauseButton.setOnClickListener {
            if (vvv.isPlaying) {
                vvv.pause()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (vvv.isPlaying) vvv.pause()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}

