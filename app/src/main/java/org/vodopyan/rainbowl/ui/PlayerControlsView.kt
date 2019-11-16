package org.vodopyan.rainbowl.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.player_controls_view.view.*
import org.vodopyan.rainbowl.R
import org.vodopyan.rainbowl.model.PlayerState
import org.vodopyan.rainbowl.utils.observe

/**
 * Controls for an audio player
 */
@SuppressLint("ViewConstructor")
class PlayerControlsView<Parent>(parent: Parent, attrs: AttributeSet? = null)
    : ConstraintLayout(parent, attrs) where Parent: Context, Parent: LifecycleOwner
{
    val state: MutableLiveData<PlayerState> = MutableLiveData()

    init {
        val view = LayoutInflater.from(parent).inflate(R.layout.player_controls_view, /*root=*/this, /*attachToRoot=*/true)

        state.observe(parent) { playerState ->
            view.name.text = playerState.sound.name
            view.playButton.setOnClickListener { playerState.isPlaying = true }
            view.stopButton.setOnClickListener { playerState.isPlaying = false }

            // TODO: make UI react on state changes
            view.volumeSeekBar.setOnSeekBarChangeListener(buildVolumeBarChangeListener(playerState, view.volumeSeekBar))
        }
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