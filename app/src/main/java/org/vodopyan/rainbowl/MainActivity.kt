package org.vodopyan.rainbowl

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.rain)
        mediaPlayer.isLooping = true
    }

    fun startPlayer(view: View) {
        mediaPlayer.start()
    }

    fun stopPlayer(view: View) {
        mediaPlayer.pause()
    }
}
