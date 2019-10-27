package org.vodopyan.rainbowl.ui

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.vodopyan.rainbowl.R
import org.vodopyan.rainbowl.model.AppDataModel
import org.vodopyan.rainbowl.model.PlayerState


class MainScreenActivity : AppCompatActivity() {
    val dataModel by inject<AppDataModel>()
    val playerState = dataModel.players.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: make UI react on state changes
        volumeSeekBar.setOnSeekBarChangeListener(buildVolumeBarChangeListener(playerState, volumeSeekBar))
        playButton.setOnClickListener { playerState.isPlaying = true }
        stopButton.setOnClickListener { playerState.isPlaying = false }
    }
}

fun buildVolumeBarChangeListener(mediaPlayer: PlayerState, volumeBar: SeekBar): SeekBar.OnSeekBarChangeListener {
    fun SeekBar.normalProgress() = (progress - min) * 1.0 / (max - min)

    mediaPlayer.volume = volumeBar.normalProgress()

    return object : SeekBar.OnSeekBarChangeListener {
        private val mediaPlayer = mediaPlayer
        private val volumeBar = volumeBar

        override fun onProgressChanged(seekBar: SeekBar, position: Int, fromUser: Boolean) {
            assert(seekBar == volumeBar) { "Cannot apply change from a different volume bar" }
            mediaPlayer.volume = volumeBar.normalProgress()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }
}