package com.example.dangmyodang

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView

class dog_trActivity : BaseActivity(TransitionMode.HORIZON) {
    private lateinit var vv: VideoView
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_tr)

        vv = findViewById(R.id.vv)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)

        val videoUri = Uri.parse("android.resource://$packageName/" + R.raw.dog_tr)
        vv.setMediaController(MediaController(this))
        vv.setVideoURI(videoUri)

        playButton.setOnClickListener {
            if (!vv.isPlaying) {
                vv.start()
            }
        }

        pauseButton.setOnClickListener {
            if (vv.isPlaying) {
                vv.pause()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (vv.isPlaying) vv.pause()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }
    }
}

